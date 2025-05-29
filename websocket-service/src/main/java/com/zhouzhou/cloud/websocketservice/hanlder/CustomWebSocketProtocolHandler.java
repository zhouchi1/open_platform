package com.zhouzhou.cloud.websocketservice.hanlder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-08-30
 * @Description: 自定义webSocket处理器
 */
@Slf4j
abstract class CustomWebSocketProtocolHandler extends MessageToMessageDecoder<WebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        if (frame instanceof PingWebSocketFrame) {
            frame.content().retain();
            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content()));
            log.info("Received Ping message");
            return;
        }
        if (frame instanceof PongWebSocketFrame) {
            frame.content().retain();
            ctx.channel().writeAndFlush(new PingWebSocketFrame(frame.content()));
            log.info("Received Pong message");
            return;
        }

        out.add(frame.retain());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }
}
