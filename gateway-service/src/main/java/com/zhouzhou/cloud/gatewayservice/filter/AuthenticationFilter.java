package com.zhouzhou.cloud.gatewayservice.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zhouzhou.cloud.common.dto.MessageDTO;
import com.zhouzhou.cloud.common.dto.UserNameAndPasswordDTO;
import com.zhouzhou.cloud.common.service.dto.UserLoginDTO;
import com.zhouzhou.cloud.common.service.excepetions.BizException;
import com.zhouzhou.cloud.common.service.interfaces.AuthServiceApi;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.gatewayservice.rabbitmqproducer.RabbitMqSenderApi;
import jakarta.annotation.Resource;
import org.apache.dubbo.common.utils.JsonUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-10
 * @Description: 授权过滤器 + websocket连接代理
 */
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @DubboReference(version = "1.0.0", timeout = 3000)
    private AuthServiceApi authServiceApi;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RabbitMqSenderApi rabbitMqSenderApi;

    @Resource
    private NamingService namingService;

    @Resource
    private WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().value();
        ServerHttpRequest request = exchange.getRequest();
        URI originalUri = request.getURI();

        // 登录授权
        if ("/login".equals(path) && "POST".equalsIgnoreCase(exchange.getRequest().getMethod().toString())) {
            return exchange.getRequest().getBody().next()
                    .flatMap(buffer -> {
                        String body = buffer.toString(StandardCharsets.UTF_8);
                        log.debug("Login request body: {}", body);

                        UserNameAndPasswordDTO userNameAndPasswordDTO = JSONObject.parseObject(body, UserNameAndPasswordDTO.class);

                        // 使用 Dubbo 同步调用放到弹性调度器
                        return Mono.fromCallable(() -> authServiceApi.getTokenFromAuthServer(userNameAndPasswordDTO))
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(token -> {

                                    String finalAddress;

                                    // 如果授权成功 正确的生成了Token 从Nacos注册中心中挑选一台Netty服务器用于连接 将连接映射信息保存到Redis中 用于后续指定机器进行连接
                                    try {
                                        List<Instance> instances = namingService.getAllInstances("websocket-service");

                                        // 随机挑选一台netty服务器 用于连接
                                        Instance selected = instances.get(new Random().nextInt(instances.size()));
                                        String address = selected.getIp() + ":" + selected.getPort();
                                        finalAddress = address;

                                        // 根据token查询用户信息
                                        UserLoginDTO userLoginDTO = JSONObject.parseObject((String) redisUtil.get(token), UserLoginDTO.class);

                                        // 将映射信息保存到Redis中
                                        redisUtil.set(userLoginDTO.getUserResp().getUserId().toString(), address, 3600);

                                    } catch (NacosException e) {
                                        e.printStackTrace();
                                        return Mono.error(e);
                                    }

                                    // 写入响应
                                    exchange.getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
                                    exchange.getResponse().getHeaders().add("X-Netty-Server", finalAddress);
                                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                                    // 编码返回给客户端
                                    Map<String, String> result = new HashMap<>();
                                    result.put("accessToken", token);
                                    result.put("nettyServerAddress", finalAddress);
                                    byte[] respBytes = JsonUtils.toJson(result).getBytes(StandardCharsets.UTF_8);
                                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                                            .bufferFactory().wrap(respBytes)));
                                });
                    });
        }

        // websocket连接
        if (originalUri.getPath().startsWith("/open-platform/websocket")) {

            // 解析参数 ip 和 port
            String ip = request.getQueryParams().getFirst("ip");
            String port = request.getQueryParams().getFirst("port");

            if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(port)) {
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                return exchange.getResponse().setComplete();
            }

            // 动态构造目标 URI
            String targetUri = String.format("ws://%s:%s" + "/open-platform/websocket", ip, port);
            URI uri = URI.create(targetUri);

            // 1. 获取原始请求
            List<String> token = request.getHeaders().get("Sec-WebSocket-Protocol");

            // 2. 构造一个新的请求，并添加 header
            ServerHttpRequest newRequest = request.mutate()
                    .header("Sec-WebSocket-Protocol", token.get(0))
                    .header("Upgrade", "websocket")
                    .header("Connection", "Upgrade")
                    .build();

            // 3. 替换请求对象
            ServerWebExchange mutatedExchange = exchange.mutate().request(newRequest).build();

            // 4. 设置新的目标地址
            mutatedExchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, uri);

            log.info(mutatedExchange.getAttribute(GATEWAY_REQUEST_URL_ATTR).toString());

            // 5. 放行
            return chain.filter(mutatedExchange);
        }

        // 消息发送
        if ("/chat/sendMessage".equals(path)){

            // 校验Token是否有效 如果无效则直接返回未授权
            List<String> token = request.getHeaders().get("Token");

            if (!authServiceApi.checkTokenValidity(token.get(0))){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // 将消息加入到MQ中 削峰
            rabbitMqSenderApi.sendTopicMessage("chat.topic", "chat.sendMessage", JSON.toJSONString(exchange.getRequest().getBody()));

            // 从请求中获取 消息的终点 查看用户当前是否存在有效的服务路由信息 如果有的话 将消息转发至指定的Netty消息推送微服务
            // 如果当前用户没有有效的服务器路由信息 则不进行推送
            String getMessageUserId = request.getQueryParams().getFirst("getMessageUserId");

            String message = request.getQueryParams().getFirst("message");

            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setTargetUserId(getMessageUserId);
            messageDTO.setMessage(message);

            if (ObjectUtils.isEmpty(getMessageUserId)){
                throw new BizException("The userId cannot be empty.");
            }

            if (!ObjectUtils.isEmpty(redisUtil.get(getMessageUserId))){
                String targetHost = (String) redisUtil.get(getMessageUserId);
                if (!ObjectUtils.isEmpty(targetHost)){
                    // 组装参数请求目标服务器
                    URI uri = UriComponentsBuilder.fromHttpUrl("http://" + targetHost)
                            .path("/chat/sendMessage")
                            .build(true)
                            .toUri();

                    // 使用 WebClient 转发
                    return webClientBuilder.build()
                            .post()
                            .uri(uri)
                            .headers(headers -> headers.addAll(request.getHeaders()))
                            .body(BodyInserters.fromValue(messageDTO))
                            .retrieve()
                            .bodyToMono(String.class)
                            .then();
                }
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
