package com.zhouzhou.cloud.websocketservice.listener;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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

    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        // 广播
        container.addMessageListener((message, pattern) -> {
            String payload = new String(message.getBody());
            // 排除掉自身的通道
            String sendMessageChannelId = extractSendMessageChannelId(payload);

            ChannelConfig.getChannelMap().forEach((channelId, channel) -> {
                if (channel.id().asLongText().equals(sendMessageChannelId)) {
                    return;
                }
                channel.writeAndFlush(new TextWebSocketFrame(payload));
            });
        }, new PatternTopic(WEBSOCKET_BROADCAST));

        // 私聊
        container.addMessageListener((message, pattern) -> {
            String payload = new String(message.getBody());

            String targetChannelId = extractTargetChannelId(payload);

            if (ObjectUtils.isEmpty(targetChannelId)) {
                return;
            }

            Channel channel = ChannelConfig.getChannel(targetChannelId);

            if (ObjectUtils.isEmpty(channel)){
                return;
            }
            channel.writeAndFlush(new TextWebSocketFrame(payload));
        }, new PatternTopic(WEBSOCKET_PRIVATE + "*"));
        return container;
    }

    private String extractTargetChannelId(String message) {
        MessageTransportDTO messageTransportDTO = JSON.parseObject(message, MessageTransportDTO.class);
        return ObjectUtils.isEmpty(messageTransportDTO.getAcceptMessageChannelId()) ? null : messageTransportDTO.getAcceptMessageChannelId();
    }

    private String extractSendMessageChannelId(String message) {
        MessageTransportDTO messageTransportDTO = JSON.parseObject(message, MessageTransportDTO.class);
        return ObjectUtils.isEmpty(messageTransportDTO.getSendMessageChannelId()) ? null : messageTransportDTO.getSendMessageChannelId();
    }
}
