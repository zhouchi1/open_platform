package com.zhouzhou.cloud.gatewayservice.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zhouzhou.cloud.common.dto.MessageDTO;
import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;
import com.zhouzhou.cloud.common.service.dto.UserLoginDTO;
import com.zhouzhou.cloud.common.service.excepetions.BizException;
import com.zhouzhou.cloud.common.service.interfaces.AuthServiceApi;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.gatewayservice.rabbitmqproducer.RabbitMqSenderApi;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.zhouzhou.cloud.common.constant.AuthConstant.UN_AUTH;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-10
 * @Description: 授权过滤器 + websocket对话
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

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().value();
        ServerHttpRequest request = exchange.getRequest();

        // 登录授权
        if ("/login".equals(path) && "POST".equalsIgnoreCase(exchange.getRequest().getMethod().toString())) {
            return exchange.getRequest().getBody().next()
                    .flatMap(buffer -> {
                        String body = buffer.toString(StandardCharsets.UTF_8);
                        log.debug("Login request body: {}", body);

                        UserIdentityConfirmDTO userIdentityConfirmDTO = JSONObject.parseObject(body, UserIdentityConfirmDTO.class);

                        // 使用 Dubbo 同步调用放到弹性调度器（弹性线程池） 防止阻塞主线程
                        return Mono.fromCallable(() -> authServiceApi.getTokenFromAuthServer(userIdentityConfirmDTO))
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(token -> {

                                    // 拒绝授权访问
                                    if (UN_AUTH.equals(token)) {
                                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                                        Map<String, Object> result = new HashMap<>();
                                        result.put("code", 401);
                                        result.put("message", "Message center denies authorized access");

                                        byte[] respBytes = JsonUtils.toJson(result).getBytes(StandardCharsets.UTF_8);
                                        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                                                .bufferFactory().wrap(respBytes)));
                                    }

                                    String finalAddress;

                                    // 如果授权成功 正确的生成了Token 从Nacos注册中心中挑选一台Netty服务器用于连接 将连接映射信息保存到Redis中 用于后续指定机器进行连接
                                    try {
                                        List<Instance> instances = namingService.getAllInstances("websocket-service");

                                        // 随机挑选一台netty服务器 用于连接
                                        Instance selected = instances.get(new Random().nextInt(instances.size()));
                                        finalAddress = selected.getIp() + ":" + selected.getPort();

                                        // 根据token查询用户信息
                                        UserLoginDTO userLoginDTO = JSONObject.parseObject((String) redisUtil.get(token), UserLoginDTO.class);

                                        // 将终端用户识别信息与服务器的映射信息保存到Redis中
                                        redisUtil.set(userLoginDTO.getUserResp().getSaasPlatformType() + ":" +
                                                userLoginDTO.getUserResp().getUserId(), finalAddress, 3600);

                                    } catch (NacosException e) {
                                        e.printStackTrace();
                                        return Mono.error(e);
                                    }

                                    // 写入响应
                                    exchange.getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
                                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                                    // 编码返回给客户端
                                    Map<String, String> result = new HashMap<>();
                                    result.put("accessToken", token);
                                    byte[] respBytes = JsonUtils.toJson(result).getBytes(StandardCharsets.UTF_8);
                                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                                            .bufferFactory().wrap(respBytes)));
                                });
                    });
        }

        // 消息发送
        if ("/chat/sendMessage".equals(path)) {

            // 校验Token是否有效 如果无效则直接返回未授权
            Optional<String> token = Objects.requireNonNull(request.getHeaders().get("Token")).stream().findFirst();

            if (!authServiceApi.checkTokenValidity(token.get())) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // 从请求中获取 消息的终点 查看用户当前是否存在有效的服务路由信息 如果有的话 将消息转发至指定的Netty消息推送分布式节点
            // 如果当前用户没有有效的服务器路由信息 则不进行推送
            // 根据token查询发送消息者用户信息
            UserLoginDTO userLoginDTO = JSONObject.parseObject((String) redisUtil.get(token.get()), UserLoginDTO.class);

            if (ObjectUtils.isEmpty(userLoginDTO.getUserResp().getUserId())) {
                throw new BizException("The message Sender userId cannot be empty.");
            }

            // 获取请求参数
            String message = request.getQueryParams().getFirst("message");
            String targetUserId = request.getQueryParams().getFirst("targetUserId");
            String appId = request.getQueryParams().getFirst("appId");

            // 组装消息传输对象
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setTargetUserId(targetUserId);
            messageDTO.setAppId(appId);
            messageDTO.setMessage(message);

            // 确认信息无误后 直接将消息发送给MQ消息微服务消费者 进行异步消息持久化操作 异步削峰
            rabbitMqSenderApi.sendTopicMessage("topicExchange", "topic.routing.key1", JSON.toJSONString(messageDTO));

            String targetHost = (String) redisUtil.get(appId + ":" + targetUserId);

            if (!ObjectUtils.isEmpty(targetHost)) {

                // 判断targetHost是否在Nacos注册中心中能找到（因为Redis中存储的与netty服务器可能发生了宕机）
                List<Instance> websocketInstances = namingService.getAllInstances("websocket-service");
                String finalTargetHost = targetHost;
                if (websocketInstances.stream().noneMatch(instance -> instance.getIp().equals(finalTargetHost.split(":")[0]) && instance.getPort() == Integer.parseInt(finalTargetHost.split(":")[1]))) {
                    // 如果找不到 则从Nacos中重新获取
                    Instance instance = websocketInstances.get(0);

                    targetHost = instance.getIp() + ":" + instance.getPort();

                    // 将映射关系赋值给当前用户的redis存储
                    redisUtil.set(userLoginDTO.getUserResp().getSaasPlatformType() + ":" + userLoginDTO.getUserResp().getUserId(), targetHost, -1);
                }

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
            } else {
                // 将消息加入到MQ中 指定离线类型发送 削峰
                rabbitMqSenderApi.sendTopicMessage("topicExchange", "topic.routing.key2", JSON.toJSONString(messageDTO));
                return exchange.getResponse().setComplete();
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
