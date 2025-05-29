package com.zhouzhou.cloud.websocketservice.service;

import com.zhouzhou.cloud.websocketservice.hanlder.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: Netty服务器
 */
@Slf4j
@Component
@RefreshScope
public class NettyServer {

    @Value("${websocket.port.nodeAPort}")
    private Integer port;

    private static EventLoopGroup bossGroup;

    private static EventLoopGroup workerGroup;

    @Resource
    private NettyBannerPrinter nettyBannerPrinter;

    @Resource
    private ThreadPoolTaskExecutor myExecutor;

    @Resource
    private WebSocketChannelInitializer webSocketChannelInitializer;

    public NettyServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
    }

    @PostConstruct
    public void init() {
        ExecutorService executorService = myExecutor.getThreadPoolExecutor();
        executorService.submit(() -> {
            try {
                start();
            } catch (InterruptedException | UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    public void start() throws InterruptedException, UnknownHostException {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, THREAD_WAIT_NUM)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(webSocketChannelInitializer);
            ChannelFuture future = bootstrap.bind(port).sync();

            String ip = InetAddress.getLocalHost().getHostAddress();

            nettyBannerPrinter.printBanner(ip, port);

            future.channel().closeFuture().sync();
        } finally {
            log.error("Warning！！！Netty-Websocket-Cluster【IP + Port：" + InetAddress.getLocalHost() + ":" + port + "】Offline or failed to start，Communication service failure！");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
