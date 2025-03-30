package com.zhouzhou.cloud.websocketservice.listener;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.UUID;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 消息分布式中转监听器
 */
@Slf4j
@RefreshScope
@Configuration
public class RedisMessageListener {

    @Resource
    private RedisConnectionFactory factory;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${websocket.port.nodeAPort}")
    private Integer port;

    @Bean
    public RedisMessageListenerContainer container() throws UnknownHostException {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        // 广播
        container.addMessageListener((message, pattern) -> {
            String payload = new String(message.getBody());

            MessageTransportDTO messageTransportDTO = JSON.parseObject(payload, MessageTransportDTO.class);
            // 排除掉自身的通道
            String sendMessageChannelId = messageTransportDTO.getMessageSendUserInfoDTO().getChannelId();

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

            MessageTransportDTO messageTransportDTO = JSON.parseObject(payload, MessageTransportDTO.class);
            messageTransportDTO.setMessageId(UUID.randomUUID().toString());

            String targetChannelId = messageTransportDTO.getMessageAcceptUserInfoDTO().getChannelId();

            if (ObjectUtils.isEmpty(targetChannelId)) {
                return;
            }

            Channel channel = ChannelConfig.getChannel(targetChannelId);

            if (ObjectUtils.isEmpty(channel)) {
                return;
            }
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageTransportDTO)));

            // 将消息发送至Redis中
            stringRedisTemplate.opsForHash().put(OFFLINE_MESSAGE_BY_USER + messageTransportDTO.getMessageAcceptUserInfoDTO().getUserId(), messageTransportDTO.getMessageId(), JSON.toJSONString(messageTransportDTO));

        }, new PatternTopic(WEBSOCKET_PRIVATE));
        return container;
    }
}
