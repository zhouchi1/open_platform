package com.zhouzhou.cloud.websocketservice.hanlder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;


public class PingHeartBeatHandler extends SimpleChannelInboundHandler<PingWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingWebSocketFrame pingWebSocketFrame) {
        if (pingWebSocketFrame != null){
            ctx.channel().writeAndFlush(new PongWebSocketFrame());
        }
    }
}
