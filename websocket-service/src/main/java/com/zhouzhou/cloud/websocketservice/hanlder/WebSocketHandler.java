package com.zhouzhou.cloud.websocketservice.hanlder;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final StringRedisTemplate stringRedisTemplate;

    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public WebSocketHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {

        String message = msg.text();
        if (isBroadcast(message)) {
            stringRedisTemplate.convertAndSend(WEBSOCKET_BROADCAST, message);
        } else {
            String targetUserId = extractTargetUserId(message);
            String targetChannelId = stringRedisTemplate.opsForValue().get(USER + targetUserId);
            if (targetChannelId != null) {
                stringRedisTemplate.convertAndSend(WEBSOCKET_PRIVATE + targetChannelId, message);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);

        String channel = WEBSOCKET_PRIVATE + ctx.channel().id().asLongText();

        channels.add(ctx.channel());

        ChannelConfig.addChannel(ctx.channel().id().asLongText(), ctx.channel());

        Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()).getConnection().subscribe((message, pattern) -> ctx.writeAndFlush(new TextWebSocketFrame(message.toString())), channel.getBytes());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ChannelConfig.removeChannel(ctx.channel().id().asLongText());
        channels.remove(ctx.channel());
    }

    private boolean isBroadcast(String message) {
        MessageTransportDTO messageTransportDTO = JSON.parseObject(message, MessageTransportDTO.class);
        return messageTransportDTO.isBroadcast();
    }

    private String extractTargetUserId(String message) {
        MessageTransportDTO messageTransportDTO = JSON.parseObject(message, MessageTransportDTO.class);
        return ObjectUtils.isEmpty(messageTransportDTO.getAcceptMessageUserId()) ? null : messageTransportDTO.getAcceptMessageUserId();
    }
}
