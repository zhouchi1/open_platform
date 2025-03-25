package com.zhouzhou.cloud.websocketservice.service;

import com.zhouzhou.cloud.websocketservice.hanlder.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioServerSocketChannel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

@Slf4j
@Component
@RefreshScope
public class NettyServer {

    @Value("${websocket.port}")
    private Integer port;

    private static EventLoopGroup bossGroup;

    private static EventLoopGroup workerGroup;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ThreadPoolTaskExecutor myExecutor;

    @Resource
    private TokenService tokenService;

    public NettyServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
    }

    @PostConstruct
    public void init() {
        ExecutorService executorService = myExecutor.getThreadPoolExecutor();
        executorService.submit(() -> {
            try {
                start(port);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void start(int port) throws InterruptedException {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, THREAD_WAIT_NUM)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new WebSocketChannelInitializer(tokenService, stringRedisTemplate));
            ChannelFuture future = bootstrap.bind(port).sync();

            log.info("\n" +
                    "+-----------------------------------------------------------------+\n" +
                    "|   _   _      _ _                 _        _              _      |\n" +
                    "|  | \\ | |    | | |               | |      | |            | |     |\n" +
                    "|  |  \\| | ___| | | ___  ___  ___ | |_ ___ | |_ ___   ___ | | __  |\n" +
                    "|  | . ` |/ _ \\ | |/ _ \\/ __|/ _ \\| __/ _ \\| __/ _ \\ / _ \\| |/ /  |\n" +
                    "|  | |\\  |  __/ | |  __/\\__ \\ (_) | || (_) | || (_) | (_) |   <   |\n" +
                    "|  |_| \\_|\\___|_|_|\\___||___/\\___/ \\__\\___/ \\__\\___/ \\___/|_|\\_\\  |\n" +
                    "|                                                                 |\n" +
                    "|  Netty - Websocket - Redis - Cluster Port: " + port + " Author：Sr.Zhou |\n" +
                    "+-----------------------------------------------------------------+\n");
            future.channel().closeFuture().sync();
        } finally {
            log.error("警告！！！ Netty-Websocket服务启动发生异常 通讯服务失效！");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
