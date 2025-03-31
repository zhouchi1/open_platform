package com.zhouzhou.cloud.websocketservice.hanlder;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import com.zhouzhou.cloud.websocketservice.enums.MessageTypeEnum;
import com.zhouzhou.cloud.websocketservice.utils.AttributeKeyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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

    @Value("${websocket.port.nodeAPort}")
    private Integer port;

    private final StringRedisTemplate stringRedisTemplate;

    public WebSocketHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {


        MessageTransportDTO messageTransportDTO = JSON.parseObject(msg.text(), MessageTransportDTO.class);

        if (MessageTypeEnum.MESSAGE_CONFIRM.getCode().equals(messageTransportDTO.getMessageType().getCode())) {

            if (ObjectUtils.isEmpty(messageTransportDTO.getMessageId())) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("请输入消息Id！"));
                return;
            }

            if (ObjectUtils.isEmpty(messageTransportDTO.getMessageSendUserInfoDTO().getUserId())) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("请输入消息所属用户Id！"));
                return;
            }

            stringRedisTemplate.opsForHash().delete(OFFLINE_MESSAGE_BY_USER + messageTransportDTO.getMessageSendUserInfoDTO().getUserId()
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
                stringRedisTemplate.convertAndSend(WEBSOCKET_BROADCAST, msg.text());
            } else {
                if (ObjectUtils.isEmpty(messageTransportDTO.getMessageAcceptUserInfoDTO().getUserId())) {
                    ctx.writeAndFlush(new TextWebSocketFrame("请输入要发送消息的用户Id！"));
                    return;
                }

                Set<String> nodeSet = stringRedisTemplate.opsForSet().members(WS_NODE_STATUS);

                if (ObjectUtils.isEmpty(nodeSet)) {
                    log.error("没有在线的Netty-Websocket节点！,请检查消息服务是否正常启动！");
                    return;
                }

                int nodeSize = 0;

                for (String node : nodeSet) {
                    String channelId = (String) stringRedisTemplate.opsForHash()
                            .get(NODE_CHANNEL_USER_INFO + node, messageTransportDTO.getMessageAcceptUserInfoDTO().getUserId());

                    if (!ObjectUtils.isEmpty(channelId)) {
                        try {
                            if (Objects.equals(node, InetAddress.getLocalHost().getHostAddress() + ":" + port)) {
                                Channel channel = ChannelConfig.getChannel(channelId);

                                if (ObjectUtils.isEmpty(channel)) {
                                    stringRedisTemplate.opsForHash().delete(NODE_CHANNEL_USER_INFO + node, messageTransportDTO.getMessageAcceptUserInfoDTO().getUserId());
                                    break;
                                }
                                channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageTransportDTO)));
                            } else {
                                stringRedisTemplate.convertAndSend(WEBSOCKET_PRIVATE, msg.text());
                            }

                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }else {
                        nodeSize++;
                    }
                }

                if (nodeSize == nodeSet.size()){
                    messageTransportDTO.setMessageId(UUID.randomUUID().toString());
                    stringRedisTemplate.opsForHash().put(OFFLINE_MESSAGE_BY_USER + messageTransportDTO.getMessageAcceptUserInfoDTO().getUserId(), messageTransportDTO.getMessageId(), JSON.toJSONString(messageTransportDTO));
                }

            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);

        String channel = WEBSOCKET_PRIVATE + ctx.channel().id().asLongText();

        ChannelConfig.addChannel(ctx.channel().id().asLongText(), ctx.channel());

        Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()).getConnection().subscribe((message, pattern) -> ctx.writeAndFlush(new TextWebSocketFrame(message.toString())), channel.getBytes());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        super.channelInactive(ctx);

        ChannelConfig.removeChannel(ctx.channel().id().asLongText());

        String userId = AttributeKeyUtils.getUserIdFromChannel(ctx);

        stringRedisTemplate.opsForHash().delete(NODE_CHANNEL_USER_INFO + InetAddress.getLocalHost().getHostAddress() + ":" + port, userId);
    }

    private Boolean isBroadcast(String message) {
        MessageTransportDTO messageTransportDTO = JSON.parseObject(message, MessageTransportDTO.class);

        if (ObjectUtils.isEmpty(messageTransportDTO)) {
            return null;
        }

        return ObjectUtils.isEmpty(messageTransportDTO.isBroadcast()) ? null : messageTransportDTO.isBroadcast();
    }
}
