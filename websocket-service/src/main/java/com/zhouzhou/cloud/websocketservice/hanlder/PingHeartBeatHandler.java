package com.zhouzhou.cloud.websocketservice.hanlder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PingHeartBeatHandler extends SimpleChannelInboundHandler<PingWebSocketFrame> {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
//                log.info("发送Ping帧检测连接 TO -> " + ctx.channel().id().asLongText());
                ctx.writeAndFlush(new PingWebSocketFrame());
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PingWebSocketFrame pingWebSocketFrame) throws Exception {

    }
}
