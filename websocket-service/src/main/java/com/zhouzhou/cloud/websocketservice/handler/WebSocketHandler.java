package com.zhouzhou.cloud.websocketservice.handler;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.constant.ConnectConstants;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import com.zhouzhou.cloud.websocketservice.enums.MessageTypeEnum;
import com.zhouzhou.cloud.websocketservice.utils.AttributeKeyUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
 * @Description: 客户端消息处理
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Value("${websocket.port}")
    private Integer port;

    private final StringRedisTemplate stringRedisTemplate;

    public WebSocketHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    @SuppressWarnings("Duplicates")
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {

        MessageTransportDTO messageTransportDTO = JSON.parseObject(msg.text(), MessageTransportDTO.class);

        if (MessageTypeEnum.MESSAGE_CONFIRM.getCode().equals(messageTransportDTO.getMessageType().getCode())) {

            if (ObjectUtils.isEmpty(messageTransportDTO.getMessageId())) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("请输入已接收的消息Id！"));
                return;
            }

            if (ObjectUtils.isEmpty(messageTransportDTO.getMessageSendUserInfoDTO())) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("请输入消息所属的用户信息！"));
                return;
            }

            if (ObjectUtils.isEmpty(messageTransportDTO.getMessageSendUserInfoDTO().getUserId())) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("请输入消息所属用户Id！"));
                return;
            }

            stringRedisTemplate.opsForHash().delete(ConnectConstants.OFFLINE_MESSAGE_BY_USER + messageTransportDTO.getMessageSendUserInfoDTO().getUserId()
                    , messageTransportDTO.getMessageId());
            return;
        }

        if (MessageTypeEnum.SEND_MESSAGE.getCode().equals(messageTransportDTO.getMessageType().getCode())) {
            Boolean tag = isBroadcast(msg.text());

            if (ObjectUtils.isEmpty(tag)) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("请输入消息内容或要选择发送的消息的传播类型！"));
                return;
            }
            if (tag) {
                stringRedisTemplate.convertAndSend(WEBSOCKET_MESSAGE, msg.text());
            } else {
                if (CollectionUtils.isEmpty(messageTransportDTO.getMessageAcceptUserInfoDTO().getUserIds())) {
                    ctx.writeAndFlush(new TextWebSocketFrame("请输入要发送消息的用户Id！"));
                    return;
                }

                List<String> userIds = messageTransportDTO.getMessageAcceptUserInfoDTO().getUserIds();

                Set<String> nodeSet = stringRedisTemplate.opsForSet().members(WS_NODE_STATUS);
                if (CollectionUtils.isEmpty(nodeSet)) {
                    log.error("没有在线的Netty-Websocket节点！请检查消息服务是否正常启动！");
                    return;
                }

                userIds.parallelStream().forEach(userId -> {

                    String messageId = storeOfflineMessage(userId, messageTransportDTO);

                    for (String node : nodeSet) {
                        String channelId = (String) stringRedisTemplate.opsForHash().get(NODE_CHANNEL_USER_INFO + node, userId);

                        if (!StringUtils.isEmpty(channelId)) {
                            try {
                                if (isCurrentNode(node)) {
                                    Channel channel = ChannelConfig.getChannel(channelId);
                                    if (channel != null && channel.isActive()) {
                                        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageTransportDTO))).addListener((ChannelFutureListener) future -> {
                                            if (future.isSuccess()) {
                                                deleteOffLineMessage(messageId, userId);
                                            }
                                        });
                                    } else {
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
                });
            }
        }
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
        stringRedisTemplate.opsForZSet().add(OFFLINE_MESSAGE_BY_USER_EXPIRE, messageId, expireTime);
        return messageId;
    }

    private void deleteOffLineMessage(String messageId, String userId) {
        stringRedisTemplate.opsForHash().delete(OFFLINE_MESSAGE_BY_USER + userId, messageId);
        stringRedisTemplate.opsForZSet().remove(OFFLINE_MESSAGE_BY_USER_EXPIRE, messageId);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        log.info("开始清理连接信息");

        super.channelInactive(ctx);

        ChannelConfig.removeChannel(ctx.channel().id().asLongText());

        String userId = AttributeKeyUtils.getUserIdFromChannel(ctx);

        if (ObjectUtils.isEmpty(userId)) {
            log.info("清理连接失败 未查询到userId信息");
            return;
        }

        String currentChannelId = (String) stringRedisTemplate.opsForHash().get(NODE_CHANNEL_USER_INFO + InetAddress.getLocalHost().getHostAddress() + ":" + port, userId);

        if (ObjectUtils.isEmpty(currentChannelId)) {
            log.error("未查询到用户绑定的通道信息");
            return;
        }

        String cleanString = currentChannelId.substring(1, currentChannelId.length() - 1);

        log.info("redis中保存的channelId：" + cleanString);
        log.info("内存中保存的channelId：" + ctx.channel().id().asLongText());

        if (ctx.channel().id().asLongText().equals(cleanString)) {
            log.info("清理连接信息中");
            stringRedisTemplate.opsForHash().delete(ConnectConstants.NODE_CHANNEL_USER_INFO + InetAddress.getLocalHost().getHostAddress() + ":" + port, userId);
        }
        log.info("管理连接清理完成");
    }

    private Boolean isBroadcast(String message) {
        MessageTransportDTO messageTransportDTO = JSON.parseObject(message, MessageTransportDTO.class);

        if (ObjectUtils.isEmpty(messageTransportDTO)) {
            return null;
        }

        return ObjectUtils.isEmpty(messageTransportDTO.isBroadcast()) ? null : messageTransportDTO.isBroadcast();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
