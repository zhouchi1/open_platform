package com.zhouzhou.cloud.websocketservice.hanlder;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


public class HeartbeatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        String text = frame.text();
        if ("HEARTBEAT".equals(text) || isHeartbeatJson(text)) {
            ctx.writeAndFlush(new TextWebSocketFrame("PONG"));
        } else {
            ctx.fireChannelRead(frame.retain());
        }
    }
    private boolean isHeartbeatJson(String txt) {
        return txt.contains("\"type\":\"HEARTBEAT\"");
    }
}
