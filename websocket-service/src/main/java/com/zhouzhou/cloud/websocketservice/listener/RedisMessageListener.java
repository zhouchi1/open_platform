package com.zhouzhou.cloud.websocketservice.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.Resource;

@Configuration
public class RedisMessageListener {

    @Resource
    private RedisConnectionFactory factory;

    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener((message, pattern) -> {
            // 处理广播消息
            String channel = new String(message.getChannel());

            String payload = new String(message.getBody());
            // 分发到所有连接的WebSocket客户端
        }, new PatternTopic("websocket.broadcast"));
        return container;
    }
}
