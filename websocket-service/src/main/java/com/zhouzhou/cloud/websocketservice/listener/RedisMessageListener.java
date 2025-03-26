package com.zhouzhou.cloud.websocketservice.listener;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 消息分布式中转监听器
 */
@Slf4j
@Configuration
public class RedisMessageListener {

    @Resource
    private RedisConnectionFactory factory;

    @Resource
    private ChannelGroup webSocketChannels;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        // 广播
        container.addMessageListener((message, pattern) -> {
            String payload = new String(message.getBody());
            webSocketChannels.writeAndFlush(new TextWebSocketFrame(payload));
        }, new PatternTopic(WEBSOCKET_BROADCAST));

        // 私聊
        container.addMessageListener((message, pattern) -> {
            String payload = new String(message.getBody());

            String targetUserId = extractTargetUserId(payload);

            if (ObjectUtils.isEmpty(targetUserId)) {
                return;
            }
            String targetChannelId = stringRedisTemplate.opsForValue().get(USER + targetUserId);

            if (ObjectUtils.isEmpty(targetChannelId)){
                return;
            }

            Channel channel = ChannelConfig.getChannel(targetChannelId);

            if (ObjectUtils.isEmpty(channel)){
                return;
            }
            channel.writeAndFlush(new TextWebSocketFrame(payload));
        }, new PatternTopic(WEBSOCKET_PRIVATE));
        return container;
    }

    private String extractTargetUserId(String message) {
        MessageTransportDTO messageTransportDTO = JSON.parseObject(message, MessageTransportDTO.class);
        return ObjectUtils.isEmpty(messageTransportDTO.getAcceptMessageUserId()) ? null : messageTransportDTO.getAcceptMessageUserId();
    }
}
