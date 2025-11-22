package com.zhouzhou.cloud.websocketservice.service;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.common.dto.MessageDTO;
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

    public void sendPrivateMessage(MessageDTO messageDTO) {
        List<String> userIds = messageDTO.getTargetUserIds();

        if (CollectionUtils.isEmpty(userIds)) {
            throw new BizException("请输入接收消息的用户ID列表");
        }

        Set<String> nodeSet = stringRedisTemplate.opsForSet().members(WS_NODE_STATUS);
        if (CollectionUtils.isEmpty(nodeSet)) {
            log.error("没有在线的Netty-Websocket节点！请检查消息服务是否正常启动！");
            return;
        }

        userIds.parallelStream().forEach(userId -> {

            String messageId = storeOfflineMessage(userId, messageDTO);

            AtomicBoolean deleteOfflineMessage = new AtomicBoolean(false);

            for (String node : nodeSet) {
                String channelId = (String) stringRedisTemplate.opsForHash().get(NODE_CHANNEL_USER_INFO + node, userId);

                if (!StringUtils.isEmpty(channelId)) {
                    log.info("已找到对应本台服务器的node节点，进行消息发送：，channelId：" + channelId);
                    try {
                        if (isCurrentNode(node)) {
                            // 需要去除前后的"字符
                            String cleanChannelId = channelId.substring(1, channelId.length() - 1);
                            Channel channel = ChannelConfig.getChannel(cleanChannelId);
                            if (channel != null && channel.isActive()) {
                                log.info("消息发送中，消息内容：" + JSON.toJSONString(messageDTO));
                                channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageDTO))).addListener((ChannelFutureListener) future -> {

                                    log.info("消息发送成功,future状态响应为：" + future.isSuccess());
                                    if (future.isSuccess()) {
                                        deleteOffLineMessage(messageId, userId);
                                    }
                                });
                            } else {
                                stringRedisTemplate.opsForHash().delete(NODE_CHANNEL_USER_INFO + node, userId);
                            }
                        } else {
                            messageDTO.setNotOnThisNodeUserId(userId);
                            stringRedisTemplate.convertAndSend(WEBSOCKET_MESSAGE, JSON.toJSONString(messageDTO));
                        }
                    } catch (Exception e) {
                        log.error("消息发送异常[用户ID:{} 节点:{}]", userId, node, e);
                    }
                }
            }
        });
    }

    private boolean isCurrentNode(String node) throws UnknownHostException {
        String localNode = InetAddress.getLocalHost().getHostAddress() + ":" + port;
        return Objects.equals(node, localNode);
    }

    private String storeOfflineMessage(String userId, MessageDTO message) {
        long expireTime = System.currentTimeMillis() + Duration.ofDays(3).toMillis();

        String messageId = UUID.randomUUID().toString();
        message.setMessageId(messageId);
        stringRedisTemplate.opsForHash().put(OFFLINE_MESSAGE_BY_USER + userId, messageId, JSON.toJSONString(message));
        stringRedisTemplate.opsForZSet().add(OFFLINE_MESSAGE_BY_USER_EXPIRE, messageId, (double) expireTime);
        return messageId;
    }

    private void deleteOffLineMessage(String messageId, String userId) {
        stringRedisTemplate.opsForHash().delete(OFFLINE_MESSAGE_BY_USER + userId, messageId);
        stringRedisTemplate.opsForZSet().remove(OFFLINE_MESSAGE_BY_USER_EXPIRE, messageId);
    }
}
