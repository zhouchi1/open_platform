package com.zhouzhou.cloud.websocketservice.service;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.common.service.excepetions.BizException;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 消息发送服务
 */
@Slf4j
@Service
public class SendMessageService {

    @Value("${websocket.port}")
    private Integer port;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void sendBroadcastMessage(MessageTransportDTO message) {
        message.setMessageId(IdUtil.getSnowflakeNextIdStr());
        stringRedisTemplate.convertAndSend(WEBSOCKET_MESSAGE, JSON.toJSONString(message));
    }

    public void sendPrivateMessage(MessageTransportDTO messageTransportDTO) {
        List<String> userIds = messageTransportDTO.getMessageAcceptUserInfoDTO().getUserIds();

        if (CollectionUtils.isEmpty(userIds)) {
            throw new BizException("请输入接收消息的用户ID列表");
        }

        Set<String> nodeSet = stringRedisTemplate.opsForSet().members(WS_NODE_STATUS);
        if (CollectionUtils.isEmpty(nodeSet)) {
            log.error("没有在线的Netty-Websocket节点！请检查消息服务是否正常启动！");
            return;
        }

        userIds.parallelStream().forEach(userId -> {

            String messageId = storeOfflineMessage(userId, messageTransportDTO);

            AtomicBoolean deleteOfflineMessage = new AtomicBoolean(false);

            for (String node : nodeSet) {
                String channelId = (String) stringRedisTemplate.opsForHash().get(NODE_CHANNEL_USER_INFO + node, userId);

                if (!StringUtils.isEmpty(channelId)) {
                    try {
                        if (isCurrentNode(node)) {
                            Channel channel = ChannelConfig.getChannel(channelId);
                            if (channel != null && channel.isActive()) {
                                channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageTransportDTO))).addListener((ChannelFutureListener) future -> {
                                    if (future.isSuccess()) {
                                        deleteOfflineMessage.set(true);
                                    } else {
                                        stringRedisTemplate.opsForValue().set(messageId, userId);
                                    }
                                });
                            } else {
                                stringRedisTemplate.opsForValue().set(messageId, userId);
                                stringRedisTemplate.opsForHash().delete(NODE_CHANNEL_USER_INFO + node, userId);
                            }
                        } else {
                            messageTransportDTO.getMessageAcceptUserInfoDTO().setCurrentUserId(userId);
                            stringRedisTemplate.convertAndSend(WEBSOCKET_MESSAGE, JSON.toJSONString(messageTransportDTO));
                        }
                    } catch (Exception e) {
                        log.error("消息发送异常[用户ID:{} 节点:{}]", userId, node, e);
                    }
                }
            }

            if (deleteOfflineMessage.get()) {
                deleteOffLineMessage(messageId, userId);
            }
        });
    }

    private boolean isCurrentNode(String node) throws UnknownHostException {
        String localNode = InetAddress.getLocalHost().getHostAddress() + ":" + port;
        return Objects.equals(node, localNode);
    }

    private String storeOfflineMessage(String userId, MessageTransportDTO message) {
        long expireTime = System.currentTimeMillis() + Duration.ofDays(3).toMillis();

        String messageId = UUID.randomUUID().toString();
        message.setMessageId(messageId);
        stringRedisTemplate.opsForHash().put(OFFLINE_MESSAGE_BY_USER + userId, messageId, JSON.toJSONString(message));
        stringRedisTemplate.opsForHash().put(OFFLINE_MESSAGE_BY_USER_EXPIRE, messageId, String.valueOf(expireTime));
        return messageId;
    }

    private void deleteOffLineMessage(String messageId, String userId) {
        stringRedisTemplate.opsForHash().delete(OFFLINE_MESSAGE_BY_USER + userId, messageId);
        stringRedisTemplate.opsForZSet().remove(OFFLINE_MESSAGE_BY_USER_EXPIRE, messageId);
    }
}
