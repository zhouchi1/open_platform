package com.zhouzhou.cloud.websocketservice.config;

import com.zhouzhou.cloud.websocketservice.service.NettyServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShutdownHookConfig {

    @Resource
    private NettyServer nettyServer;

    @PostConstruct
    public void registerShutdownHook() {
        log.info("注册JVM关闭钩子");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("JVM关闭钩子被触发");
            nettyServer.shutdown();
        }));
    }
}