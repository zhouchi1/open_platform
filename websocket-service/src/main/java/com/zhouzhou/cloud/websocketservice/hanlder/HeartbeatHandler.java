package com.zhouzhou.cloud.websocketservice.hanlder;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-29
 * @Description: 心跳在线状态处理拦截器
 */
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
