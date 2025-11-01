package com.zhouzhou.cloud.websocketservice.listener;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.common.entity.MessageCenter;
import com.zhouzhou.cloud.common.enums.messageservice.MessageKindEnum;
import com.zhouzhou.cloud.common.enums.messageservice.MessageReadStatusEnum;
import com.zhouzhou.cloud.common.mapper.MessageCenterMapper;
import com.zhouzhou.cloud.common.service.excepetions.BizException;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import com.zhouzhou.cloud.websocketservice.dto.SecurityCheckCompleteDTO;
import com.zhouzhou.cloud.websocketservice.utils.AttributeKeyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 消息分布式中转监听器
 */
@Slf4j
@Component
public class RedisMessageListener {

    @Value("${websocket.port}")
    private Integer port;

    @Resource
    private RedisConnectionFactory factory;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MessageCenterMapper messageCenterMapper;

    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        container.addMessageListener((message, pattern) -> {

            String payload = new String(message.getBody());

            MessageTransportDTO messageTransportDTO = JSON.parseObject(payload, MessageTransportDTO.class);

            // 是否是广播消息(请注意 这里后期需要进行修改 把！去掉（因为当前调用此接口的地方都没有进行字段传递默认为false,后续正常入参true即可解决）)
            if (!messageTransportDTO.isBroadcast()) {
                try {
                    if (Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(SOCKET_MESSAGE_ID + InetAddress.getLocalHost().getHostName() + messageTransportDTO.getMessageId(), messageTransportDTO.getMessage(), Duration.ofMinutes(1)))) {
                        log.info("消息ID重复[用户ID:{}]", messageTransportDTO.getMessageSendUserInfoDTO().getUserId());
                        return;
                    }
                } catch (UnknownHostException e) {
                    throw new BizException("获取本机IP失败");
                }

                String sendMessageUserId = messageTransportDTO.getMessageSendUserInfoDTO().getUserId();

                String sendMessageChannelId;
                try {
                    sendMessageChannelId = (String) stringRedisTemplate.opsForHash().get(NODE_CHANNEL_USER_INFO + InetAddress.getLocalHost().getHostAddress() + ":" + port, sendMessageUserId);

                    String finalSendMessageChannelId = sendMessageChannelId;

                    ChannelConfig.getChannelMap().forEach((channelId, channel) -> {
                        if (channel.id().asLongText().equals(finalSendMessageChannelId)) {
                            return;
                        }
                        channel.writeAndFlush(new TextWebSocketFrame(payload));
                        SecurityCheckCompleteDTO securityInfo = channel.attr(AttributeKeyUtils.SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).get();
                        if (ObjectUtils.isEmpty(securityInfo)) {
                            return;
                        }
                        // 消息中心-消息持久化
                        MessageCenter messageCenter = new MessageCenter();
                        messageCenter.setMessageCode(messageTransportDTO.getMessageId());
                        messageCenter.setMessage(messageTransportDTO.getMessage());
                        messageCenter.setType(MessageKindEnum.JOIN_MESSAGE);
                        messageCenter.setMessageStatus(MessageReadStatusEnum.UNREAD);
                        messageCenter.setUserCode(securityInfo.getUserCode());
                        messageCenterMapper.insert(messageCenter);
                    });
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            } else {

                String targetUserId = messageTransportDTO.getMessageAcceptUserInfoDTO().getCurrentUserId();

                if (ObjectUtils.isEmpty(targetUserId)) {
                    return;
                }

                String targetChannelId;
                try {
                    targetChannelId = (String) stringRedisTemplate.opsForHash().get(NODE_CHANNEL_USER_INFO + InetAddress.getLocalHost().getHostAddress() + ":" + port, targetUserId);

                    if (ObjectUtils.isEmpty(targetChannelId)) {
                        return;
                    }

                    Channel channel = ChannelConfig.getChannel(targetChannelId);

                    if (ObjectUtils.isEmpty(channel)) {
                        return;
                    }

                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageTransportDTO))).addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            stringRedisTemplate.opsForHash().delete(OFFLINE_MESSAGE_BY_USER + targetUserId, messageTransportDTO.getMessageId());
                        }
                    });
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }, new PatternTopic(WEBSOCKET_MESSAGE));
        return container;
    }
}
