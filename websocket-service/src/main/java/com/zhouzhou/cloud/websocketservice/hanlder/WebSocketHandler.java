package com.zhouzhou.cloud.websocketservice.hanlder;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import io.netty.channel.Channel;
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
import java.util.concurrent.ConcurrentHashMap;

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

        log.info("收到消息：" + message);

        if (isBroadcast(message)) {
            log.info("广播消息：" + message);
            stringRedisTemplate.convertAndSend("websocket.broadcast", message);
        } else {
            log.info("私聊消息：" + message);
            String targetUserId = extractTargetUserId(message);
            String targetChannelId = stringRedisTemplate.opsForValue().get("user:" + targetUserId);
            if (targetChannelId != null) {
                stringRedisTemplate.convertAndSend("websocket.send." + targetChannelId, message);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 构造实例专属频道名称
        String channel = "websocket.send." + ctx.channel().id().asLongText();

        channels.add(ctx.channel());
        ChannelConfig.addChannel(ctx.channel().id().asLongText(), ctx.channel());

        ConcurrentHashMap<String, Channel> channelConcurrentHashMap = ChannelConfig.getChannelMap();

        log.info("channelConcurrentHashMap" + channelConcurrentHashMap);

        // 获取 Redis 连接并订阅指定频道
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
