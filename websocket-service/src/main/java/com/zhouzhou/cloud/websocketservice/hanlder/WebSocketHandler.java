package com.zhouzhou.cloud.websocketservice.hanlder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Objects;

@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final StringRedisTemplate stringRedisTemplate;

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

        // 获取 Redis 连接并订阅指定频道
        Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()).getConnection().subscribe((message, pattern) -> ctx.writeAndFlush(new TextWebSocketFrame(message.toString())), channel.getBytes());
    }

    private boolean isBroadcast(String message) {
        // JSON反序列化消息实体 从中取出消息类型
        return true;
    }

    private String extractTargetUserId(String message) {
        // JSON反序列化消息实体 从中取出目标用户ID
        return null;
    }
}
