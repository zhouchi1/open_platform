package com.zhouzhou.cloud.websocketservice.config;

import com.zhouzhou.cloud.websocketservice.hanlder.AuthHandler;
import com.zhouzhou.cloud.websocketservice.hanlder.HeartbeatIdleHandler;
import com.zhouzhou.cloud.websocketservice.hanlder.WebSocketChannelInitializer;
import com.zhouzhou.cloud.websocketservice.hanlder.WebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 通道组配置
 */
@Configuration
public class NettyConfig {

    @Bean
    public AuthHandler authHandler() {
        return new AuthHandler();
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler();
    }

    @Bean
    public HeartbeatIdleHandler heartbeatIdleHandler() {
        return new HeartbeatIdleHandler();
    }

    @Bean
    public WebSocketChannelInitializer webSocketChannelInitializer(AuthHandler authHandler, WebSocketHandler webSocketHandler, HeartbeatIdleHandler heartbeatIdleHandler) {
        return new WebSocketChannelInitializer(authHandler, webSocketHandler, heartbeatIdleHandler);
    }
}
