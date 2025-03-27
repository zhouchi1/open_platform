package com.zhouzhou.cloud.websocketservice.hanlder;

import com.zhouzhou.cloud.websocketservice.service.TokenService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 处理器链
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final TokenService tokenService;

    private final StringRedisTemplate stringRedisTemplate;

    public WebSocketChannelInitializer(TokenService tokenService, StringRedisTemplate stringRedisTemplate) {
        this.tokenService = tokenService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new IdleStateHandler(READER_IDLE_TIME, WRITER_IDLE_TIME, ALL_IDLE_TIME, TimeUnit.SECONDS));
        pipeline.addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new AuthHandler(tokenService, stringRedisTemplate));
        pipeline.addLast(new WebSocketHandler(stringRedisTemplate));
        pipeline.addLast(new CustomWebSocketServerProtocolHandler(WEBSOCKET_URL, SUB_PROTOCOLS, ALLOW_EXTENSIONS, MAX_FRAME_SIZE,true));
        pipeline.addLast(new PingHeartBeatHandler());
    }
}
