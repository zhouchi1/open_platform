package com.zhouzhou.cloud.websocketservice.hanlder;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

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
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new IdleStateHandler(READER_IDLE_TIME, WRITER_IDLE_TIME, ALL_IDLE_TIME, TimeUnit.SECONDS));
        pipeline.addLast(new HeartbeatHandler());
        pipeline.addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(webSocketHandler);
        pipeline.addLast(authHandler);
        pipeline.addLast(new CustomWebSocketServerProtocolHandler(WEBSOCKET_URL, SUB_PROTOCOLS, ALLOW_EXTENSIONS, MAX_FRAME_SIZE,true));
    }
}
