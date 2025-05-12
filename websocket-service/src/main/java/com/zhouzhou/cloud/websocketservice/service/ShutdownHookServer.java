package com.zhouzhou.cloud.websocketservice.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@RefreshScope
@Component
public class ShutdownHookServer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${websocket.port.nodeAPort}")
    private Integer port;

    @PostConstruct
    public void registerHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // 删除注册在该节点上所有客户端连接的路由对应关系


            } catch (Exception e) {
                log.error("ShutdownHook 清理用户与服务端映射关系失败", e);
            }
        }));
    }
}
