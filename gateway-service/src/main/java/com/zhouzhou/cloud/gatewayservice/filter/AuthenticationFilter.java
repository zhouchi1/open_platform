package com.zhouzhou.cloud.gatewayservice.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zhouzhou.cloud.common.dto.MessageDTO;
import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;
import com.zhouzhou.cloud.common.service.dto.UserLoginDTO;
import com.zhouzhou.cloud.common.service.interfaces.AuthRpcServer;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.gatewayservice.rabbitmqproducer.RabbitMqSenderApi;
import jakarta.annotation.Resource;
import org.apache.dubbo.common.utils.JsonUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.zhouzhou.cloud.common.constant.AuthConstant.UN_AUTH;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-10
 * @Description: 安全、响应式、状态码清晰的全局认证过滤器
 */
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @DubboReference(version = "1.0.0", timeout = 3000)
    private AuthRpcServer authRpcServer;

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
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        String method = request.getMethod().name();

        log.info("Processing request: {} {}", method, path);

        try {
            if ("/login".equals(path) && "POST".equalsIgnoreCase(method)) {
                return handleLogin(exchange);
            }

            if ("/chat/sendMessage".equals(path)) {
                return handleMessageSend(exchange);
            }

            return chain.filter(exchange);

        } catch (Exception e) {
            log.error("Unexpected error in filter", e);
            return buildErrorResponse(exchange, "Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 登录处理
     */
    private Mono<Void> handleLogin(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();

        return DataBufferUtils.join(request.getBody())
                .flatMap(dataBuffer -> {
                    byte[] bodyBytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bodyBytes);
                    DataBufferUtils.release(dataBuffer);

                    String body = new String(bodyBytes, StandardCharsets.UTF_8);
                    log.info("Login request body: {}", body);

                    if (body.trim().isEmpty()) {
                        return buildErrorResponse(exchange, "Request body is empty", HttpStatus.BAD_REQUEST);
                    }

                    UserIdentityConfirmDTO userDTO;
                    try {
                        userDTO = JSONObject.parseObject(body, UserIdentityConfirmDTO.class);
                    } catch (Exception e) {
                        return buildErrorResponse(exchange, "Invalid JSON format", HttpStatus.BAD_REQUEST);
                    }

                    // ------------------------------
                    // ⭐ 使用 CachedBodyOutputMessage 缓存 body
                    // ------------------------------
                    DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
                    CachedBodyOutputMessage cachedBody = new CachedBodyOutputMessage(exchange, request.getHeaders());

                    // 将 bodyBytes 包装成 DataBuffer
                    Flux<DataBuffer> bodyFlux = Flux.defer(() -> Mono.just(bufferFactory.wrap(bodyBytes)));

                    // 写入 CachedBodyOutputMessage
                    return cachedBody.writeWith(bodyFlux)
                            .then(Mono.defer(() -> {
                                // 用 decorator 让下游读取 cached body
                                ServerHttpRequestDecorator decoratedRequest =
                                        new ServerHttpRequestDecorator(request) {
                                            @Override
                                            public Flux<DataBuffer> getBody() {
                                                return cachedBody.getBody();
                                            }

                                            @Override
                                            public HttpHeaders getHeaders() {
                                                HttpHeaders headers = new HttpHeaders();
                                                headers.putAll(super.getHeaders());
                                                headers.setContentLength(bodyBytes.length);
                                                if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
                                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                                }
                                                return headers;
                                            }
                                        };

                                ServerWebExchange mutatedExchange = exchange.mutate()
                                        .request(decoratedRequest)
                                        .build();

                                return processLogin(mutatedExchange, userDTO);
                            }));
                });
    }

    private Mono<Void> processLogin(ServerWebExchange exchange, UserIdentityConfirmDTO userDTO) {
        return Mono.fromCallable(() -> authRpcServer.getTokenFromAuthServer(userDTO))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(token -> {
                    if (UN_AUTH.equals(token)) {
                        return buildUnauthorizedResponse(exchange, "Authentication failed");
                    }
                    return processLoginSuccess(exchange, token);
                })
                .onErrorResume(throwable -> {
                    log.error("Login processing error", throwable);
                    if (throwable instanceof java.util.concurrent.TimeoutException) {
                        return buildErrorResponse(exchange, "Authentication service timeout", HttpStatus.GATEWAY_TIMEOUT);
                    } else if (throwable instanceof org.apache.dubbo.rpc.RpcException) {
                        return buildErrorResponse(exchange, "Authentication service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
                    } else {
                        return buildErrorResponse(exchange, "Login failed: " + throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                });
    }

    private Mono<Void> processLoginSuccess(ServerWebExchange exchange, String token) {
        try {
            List<Instance> instances = namingService.getAllInstances("websocket-service");
            Instance selected = instances.isEmpty() ? null : instances.get(new Random().nextInt(instances.size()));
            String wsAddress = selected == null ? null : selected.getIp() + ":" + selected.getPort();

            String userInfoJson = (String) redisUtil.get(token);
            UserLoginDTO userLoginDTO;
            if (userInfoJson != null) {
                try {
                    userLoginDTO = JSONObject.parseObject(userInfoJson, UserLoginDTO.class);
                    if (userLoginDTO != null && userLoginDTO.getUserResp() != null &&
                            userLoginDTO.getUserResp().getUserId() != null && wsAddress != null) {
                        redisUtil.set(userLoginDTO.getUserResp().getUserId(), wsAddress, 3600);
                    }
                } catch (Exception e) {
                    log.warn("User info parse error, continue", e);
                }
            }

            return buildLoginSuccessResponse(exchange, token, wsAddress);
        } catch (NacosException e) {
            log.error("Nacos discovery error", e);
            return buildLoginSuccessResponse(exchange, token, null);
        } catch (Exception e) {
            log.error("Unexpected login success processing error", e);
            return buildLoginSuccessResponse(exchange, token, null);
        }
    }

    private Mono<Void> buildLoginSuccessResponse(ServerWebExchange exchange, String token, String websocketAddress) {
        exchange.getResponse().getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", token);
        result.put("tokenType", "Bearer");
        result.put("expiresIn", 3600);
        if (websocketAddress != null) result.put("websocketServer", websocketAddress);

        byte[] respBytes = JsonUtils.toJson(result).getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(respBytes)));
    }

    /**
     * 消息发送处理
     */
    private Mono<Void> handleMessageSend(ServerWebExchange exchange) {
        List<String> tokenHeaders = exchange.getRequest().getHeaders().get("Token");
        if (tokenHeaders == null || tokenHeaders.isEmpty()) {
            return buildUnauthorizedResponse(exchange, "Token header is required");
        }

        String token = tokenHeaders.get(0);
        if (token == null || token.trim().isEmpty()) {
            return buildUnauthorizedResponse(exchange, "Token is empty");
        }

        return Mono.fromCallable(() -> authRpcServer.checkTokenValidity(token))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(valid -> valid ? processMessageSend(exchange, token)
                        : buildUnauthorizedResponse(exchange, "Invalid token"))
                .onErrorResume(throwable -> {
                    log.error("Token validation error", throwable);
                    return buildErrorResponse(exchange, "Token validation failed: " + throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    private Mono<Void> processMessageSend(ServerWebExchange exchange, String token) {
        ServerHttpRequest request = exchange.getRequest();
        String userInfoJson = (String) redisUtil.get(token);
        if (userInfoJson == null) return buildUnauthorizedResponse(exchange, "User information not found");

        UserLoginDTO userDTO;
        try {
            userDTO = JSONObject.parseObject(userInfoJson, UserLoginDTO.class);
        } catch (Exception e) {
            return buildErrorResponse(exchange, "Invalid user info format", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (userDTO == null || userDTO.getUserResp() == null ||
                ObjectUtils.isEmpty(userDTO.getUserResp().getUserId())) {
            return buildErrorResponse(exchange, "UserId cannot be empty", HttpStatus.BAD_REQUEST);
        }

        String message = request.getQueryParams().getFirst("message");

        List<String> targetUserIds = new ArrayList<>();
        String targetUserIdParam = request.getQueryParams().getFirst("targetUserId");
        if (targetUserIdParam != null && !targetUserIdParam.trim().isEmpty()) {
            targetUserIds = Arrays.stream(targetUserIdParam.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }

        if (ObjectUtils.isEmpty(message) || CollectionUtils.isEmpty(targetUserIds)) {
            return buildErrorResponse(exchange, "Message and targetUserId are required", HttpStatus.BAD_REQUEST);
        }

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(message);
        messageDTO.setTargetUserIds(targetUserIds);

        try {
            rabbitMqSenderApi.sendTopicMessage("topicExchange", "topic.routing.key1", JSON.toJSONString(messageDTO));
        } catch (Exception e) {
            log.warn("MQ send error, continue", e);
        }

        for (String targetUserId : targetUserIds){
            String targetHost = (String) redisUtil.get(targetUserId);
            if (!ObjectUtils.isEmpty(targetHost)) {
                return forwardMessageToWebSocket(exchange, request, messageDTO, targetHost, userDTO);
            }

            try {
                rabbitMqSenderApi.sendTopicMessage("topicExchange", "topic.routing.key2", JSON.toJSONString(messageDTO));
            } catch (Exception e) {
                log.warn("MQ offline send error, continue", e);
            }
        }

        return buildSuccessResponse(exchange, "Message sent to offline queue");
    }

    private Mono<Void> forwardMessageToWebSocket(ServerWebExchange exchange, ServerHttpRequest request,
                                                 MessageDTO messageDTO, String targetHost, UserLoginDTO userDTO) {
        try {
            List<Instance> instances = namingService.getAllInstances("websocket-service");
            String finalTargetHost = targetHost;
            boolean hostAvailable = instances.stream().anyMatch(i ->
                    i.getIp().equals(finalTargetHost.split(":")[0]) &&
                            i.getPort() == Integer.parseInt(finalTargetHost.split(":")[1]));

            if (!hostAvailable && !instances.isEmpty()) {
                Instance instance = instances.get(0);
                targetHost = instance.getIp() + ":" + instance.getPort();
                try {
                    redisUtil.set(userDTO.getUserResp().getUserId(), targetHost, -1);
                } catch (Exception e) {
                    log.warn("Update Redis mapping failed", e);
                }
            }

            URI uri = UriComponentsBuilder.fromHttpUrl("http://" + targetHost)
                    .path("/chat/sendMessage")
                    .build(true)
                    .toUri();

            return webClientBuilder.build()
                    .post()
                    .uri(uri)
                    .headers(h -> {
                        h.addAll(request.getHeaders());
                        h.remove(HttpHeaders.HOST);
                    })
                    .body(BodyInserters.fromValue(messageDTO))
                    .retrieve()
                    .bodyToMono(String.class)
                    .then(buildSuccessResponse(exchange, "Message sent successfully"))
                    .onErrorResume(throwable -> buildErrorResponse(exchange, "WebSocket forwarding failed: " + throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));

        } catch (NacosException e) {
            return buildErrorResponse(exchange, "Service discovery error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return buildErrorResponse(exchange, "Forwarding error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Mono<Void> buildSuccessResponse(ServerWebExchange exchange, String message) {
        return buildResponse(exchange, HttpStatus.OK, 200, message);
    }

    private Mono<Void> buildUnauthorizedResponse(ServerWebExchange exchange, String message) {
        return buildResponse(exchange, HttpStatus.UNAUTHORIZED, 401, message);
    }

    private Mono<Void> buildErrorResponse(ServerWebExchange exchange, String message, HttpStatus status) {
        log.error("构建错误的返回信息");
        return buildResponse(exchange, status, status.value(), message);
    }

    private Mono<Void> buildResponse(ServerWebExchange exchange, HttpStatus status, int code, String message) {

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());

        log.error(result.toString());

        byte[] respBytes = JsonUtils.toJson(result).getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(respBytes)));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
