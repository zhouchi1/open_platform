package com.zhouzhou.cloud.websocketservice.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PingHeartBeatHandler extends SimpleChannelInboundHandler<PingWebSocketFrame> {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.READER_IDLE) {
                // 此处代码在正式的生产环境中 需要使用ping数据帧 前端需要根据ping来响应pong来完成完整完整的心跳检测机制
//                ctx.writeAndFlush(new PingWebSocketFrame());
                ctx.writeAndFlush(new TextWebSocketFrame("ping"));
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PingWebSocketFrame pingWebSocketFrame) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        cause.printStackTrace();
        ctx.close();
    }
}
