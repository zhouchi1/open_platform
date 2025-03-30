package com.zhouzhou.cloud.websocketservice.hanlder;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import com.zhouzhou.cloud.websocketservice.enums.MessageTypeEnum;
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
import java.util.Objects;

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
            stringRedisTemplate.opsForHash().delete(OFFLINE_MESSAGE_BY_USER + messageTransportDTO.getMessageAcceptUserInfoDTO().getUserId(), messageTransportDTO.getMessageId());
            return;
        }

        if (MessageTypeEnum.SEND_MESSAGE.getCode().equals(messageTransportDTO.getMessageType().getCode())){
            Boolean tag = isBroadcast(msg.text());

            if (ObjectUtils.isEmpty(tag)) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("请输入消息内容或要选择发送的消息的传播类型！"));
                return;
            }

            if (tag) {
                stringRedisTemplate.convertAndSend(WEBSOCKET_BROADCAST, msg.text());
            } else {
                String targetChannelId = extractTargetChannelId(msg.text());
                if (targetChannelId != null) {
                    stringRedisTemplate.convertAndSend(WEBSOCKET_PRIVATE, msg.text());
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

        // 删除Redis中当前通道的登录信息
        stringRedisTemplate.opsForHash().delete(NODE_CHANNEL_USER_INFO + InetAddress.getLocalHost().getHostAddress() + ":" + port, ctx.channel().id().asLongText());
    }

    private Boolean isBroadcast(String message) {
        MessageTransportDTO messageTransportDTO = JSON.parseObject(message, MessageTransportDTO.class);

        if (ObjectUtils.isEmpty(messageTransportDTO)) {
            return null;
        }

        return ObjectUtils.isEmpty(messageTransportDTO.isBroadcast()) ? null : messageTransportDTO.isBroadcast();
    }

    private String extractTargetChannelId(String message) {
        MessageTransportDTO messageTransportDTO = JSON.parseObject(message, MessageTransportDTO.class);
        return ObjectUtils.isEmpty(messageTransportDTO.getMessageAcceptUserInfoDTO().getChannelId()) ? null : messageTransportDTO.getMessageAcceptUserInfoDTO().getChannelId();
    }
}
