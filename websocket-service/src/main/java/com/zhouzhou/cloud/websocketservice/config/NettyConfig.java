package com.zhouzhou.cloud.websocketservice.config;

import com.zhouzhou.cloud.websocketservice.hanlder.AuthHandler;
import com.zhouzhou.cloud.websocketservice.hanlder.WebSocketChannelInitializer;
import com.zhouzhou.cloud.websocketservice.hanlder.WebSocketHandler;
import com.zhouzhou.cloud.websocketservice.service.TokenService;
import io.netty.channel.group.ChannelGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 通道组配置
 */
@Configuration
public class NettyConfig {

    @Bean
    public AuthHandler authHandler(TokenService tokenService, StringRedisTemplate stringRedisTemplate) {
        return new AuthHandler(tokenService, stringRedisTemplate);
    }

    @Bean
    public WebSocketHandler webSocketHandler(StringRedisTemplate stringRedisTemplate) {
        return new WebSocketHandler(stringRedisTemplate);
    }

    @Bean
    public WebSocketChannelInitializer webSocketChannelInitializer(AuthHandler authHandler, WebSocketHandler webSocketHandler) {
        return new WebSocketChannelInitializer(authHandler, webSocketHandler);
    }
}
