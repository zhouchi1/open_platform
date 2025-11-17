package com.zhouzhou.cloud.websocketservice.handler;

import com.zhouzhou.cloud.websocketservice.constant.ConnectConstants;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 处理器链
 */
@ChannelHandler.Sharable
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final AuthHandler authHandler;

    private final WebSocketHandler webSocketHandler;

    public WebSocketChannelInitializer(AuthHandler authHandler, WebSocketHandler webSocketHandler) {
        this.authHandler = authHandler;
        this.webSocketHandler = webSocketHandler;
    }


    @Override
    protected void initChannel(SocketChannel ch) throws CertificateException, SSLException {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new IdleStateHandler(ConnectConstants.READER_IDLE_TIME, ConnectConstants.WRITER_IDLE_TIME, ConnectConstants.ALL_IDLE_TIME, TimeUnit.SECONDS));
        pipeline.addLast(new HttpObjectAggregator(ConnectConstants.MAX_CONTENT_LENGTH));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(webSocketHandler);
        pipeline.addLast(authHandler);
        pipeline.addLast(new CustomWebSocketServerProtocolHandler(ConnectConstants.WEBSOCKET_URL, ConnectConstants.SUB_PROTOCOLS, ConnectConstants.ALLOW_EXTENSIONS, ConnectConstants.MAX_FRAME_SIZE,true));
        pipeline.addLast(new PingHeartBeatHandler());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
