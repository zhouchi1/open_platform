package com.zhouzhou.cloud.gatewayservice.filter;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zhouzhou.cloud.common.service.dto.UserLoginDTO;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.JsonUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-29
 * @Description: websocket连接请求转发
 */
@Slf4j
@Component
public class WebSocketRoutingFilter implements GlobalFilter, Ordered {


    @Resource
    private final RedisUtil redisUtil;

    @Resource
    private final NamingService namingService;

    public WebSocketRoutingFilter(RedisUtil redisUtil,
                                  NamingService namingService) {
        this.redisUtil = redisUtil;
        this.namingService = namingService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI originalUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        ServerHttpRequest request = exchange.getRequest();

        if (originalUri != null && originalUri.getPath().startsWith("/open-platform/websocket")) {

            log.info("进入websocket连接请求处理");

            // 1. 获取Token
            HttpHeaders headers = request.getHeaders();
            Optional<String> tokenOptional = Optional.ofNullable(headers.get("Sec-WebSocket-Protocol"))
                    .flatMap(values -> values.stream().findFirst());

            if (tokenOptional.isEmpty()) {
                return unauthorizedResponse(exchange, "Message center denies authorized access");
            }

            String token = tokenOptional.get();
            log.info("获取token 进行身份授权认证: {}", token);

            // 2. 验证Token并获取用户信息 - 使用 justOrEmpty 确保执行
            return Mono.justOrEmpty((String) redisUtil.get(token))
                    .flatMap(userInfo -> {
                        log.info("Redis查询结果: {}", userInfo);

                        if (ObjectUtils.isEmpty(userInfo)) {
                            log.info("未查询到认证授权的用户访问信息 返回未授权退出");
                            return unauthorizedResponse(exchange, "Message center denies authorized access type:userInfo");
                        }

                        UserLoginDTO userLoginDTO;
                        try {
                            userLoginDTO = JSONObject.parseObject(userInfo, UserLoginDTO.class);
                        } catch (Exception e) {
                            log.error("用户信息解析失败", e);
                            return unauthorizedResponse(exchange, "用户信息解析失败");
                        }

                        // 3. 获取用户绑定的Netty服务器地址
                        String bindingKey = userLoginDTO.getUserResp().getUserId();
                        String address = (String) redisUtil.get(bindingKey);

                        log.info("获取netty服务器地址: {}", address);

                        if (ObjectUtils.isEmpty(address)) {
                            return unauthorizedResponse(exchange, "Message center denies authorized access type:address");
                        }

                        // 4. 解析地址并查找实例
                        String[] parts = address.split(":");
                        if (parts.length != 2) {
                            return unauthorizedResponse(exchange, "Message center denies authorized access type:parts");
                        }

                        String serverIp = parts[0];
                        int serverPort;
                        try {
                            serverPort = Integer.parseInt(parts[1]);
                        } catch (NumberFormatException e) {
                            return unauthorizedResponse(exchange, "Message center denies authorized access type:serverPort");
                        }

                        log.info("开始转发websocket连接至netty微服务: {}:{}", serverIp, serverPort);

                        // 5. 查找匹配的服务实例
                        return findServiceInstance(serverIp, serverPort)
                                .switchIfEmpty(rebindToAvailableNettyServer(userLoginDTO))
                                .flatMap(instance -> {
                                    String nettyPort = instance.getMetadata().get("netty-port");
                                    String targetUri = "ws://" + instance.getIp() + ":" + nettyPort + "/open-platform/websocket";

                                    ServerWebExchangeUtils.addOriginalRequestUrl(exchange, request.getURI());
                                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, URI.create(targetUri));
                                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR, "ws");

                                    log.info("route webSocket to: {}", targetUri);

                                    return chain.filter(exchange);
                                });
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        log.info("Token在Redis中不存在");
                        return unauthorizedResponse(exchange, "Message center denies authorized access type:userInfo");
                    }))
                    .onErrorResume(e -> {
                        log.error("access websocket route error", e);
                        return unauthorizedResponse(exchange, "Message center denies authorized access: netty server unavailable");
                    });
        }

        // 非WebSocket请求继续处理
        return chain.filter(exchange);
    }

    private Mono<Instance> rebindToAvailableNettyServer(UserLoginDTO userLoginDTO) {
        return Mono.fromCallable(() -> {
            // 获取所有服务实例
            List<Instance> instances = namingService.getAllInstances("websocket-service");
            if (instances == null || instances.isEmpty()) {
                throw new IllegalStateException("No Netty servers available");
            }

            // 选择第一个实例（你也可以用更复杂的选择策略）
            Instance instance = instances.get(0);

            String newBinding = instance.getIp() + ":" + instance.getPort();
            String bindingKey = userLoginDTO.getUserResp().getUserId();

            redisUtil.set(bindingKey, newBinding, -1);

            log.info("Rebind user {} to new Netty instance: {}", bindingKey, newBinding);

            // 把实例返回出去
            return instance;
        });
    }


    private Mono<Instance> findServiceInstance(String ip, int port) {
        return Mono.fromSupplier(() -> {
            try {
                return namingService.getAllInstances("websocket-service");
            } catch (Exception e) {
                throw new RuntimeException("Message center denies authorized access", e);
            }
        })
                .flatMapIterable(instances -> instances)
                .filter(instance -> instance.getIp().equals(ip) && instance.getPort() == port)
                .next();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {

        log.info("开始返回无授权响应");

        if (isWebSocketRequest(exchange)) {
            log.info("Unauthorized WebSocket access: {}", message);
            // 对于 WebSocket，不返回响应体，直接结束连接
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (!exchange.getResponse().isCommitted()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("message", message);

        byte[] respBytes = JsonUtils.toJson(result).getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(
                exchange.getResponse().bufferFactory().wrap(respBytes)
        ));
    }

    private boolean isWebSocketRequest(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getUpgrade() != null &&
                exchange.getRequest().getHeaders().getUpgrade().contains("websocket");
    }


    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
