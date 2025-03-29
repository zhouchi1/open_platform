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
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    @Value("${websocket.node.nodeANum}")
    private Integer nodeNum;

    private static EventLoopGroup bossGroup;

    private static EventLoopGroup workerGroup;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ThreadPoolTaskExecutor myExecutor;

    @Resource
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

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
                start(port);
            } catch (InterruptedException | UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 优雅启动
     * @param port 运行端口
     * @throws InterruptedException 系统中断异常
     * @throws UnknownHostException 系统未知异常
     */
    public void start(int port) throws InterruptedException, UnknownHostException {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, THREAD_WAIT_NUM)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(webSocketChannelInitializer);
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

            registerNodeHeartbeat();

            future.channel().closeFuture().sync();
        } finally {
            log.error("警告！！！Netty-Websocket服务Cluster节点【主机/IP：" + InetAddress.getLocalHost() + "】已下线 通讯服务失效！");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    /**
     * 优雅下线该分布式节点
     */
    @PreDestroy
    public void onShutdown() {
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys(NODE_ID + nodeNum + CHANNEL_ID + "*")));
    }

    public void registerNodeHeartbeat() throws UnknownHostException {

        String nodeKey = WS_NODE_STATUS + NODE_ID + nodeNum;
        stringRedisTemplate.opsForValue().set(nodeKey, "alive", 30, TimeUnit.SECONDS);

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            stringRedisTemplate.expire(nodeKey, 30, TimeUnit.SECONDS);
        }, 0, 5, TimeUnit.SECONDS);

        log.info("分布式节点【主机/IP：" + InetAddress.getLocalHost() + "】已注册到Redis中，节点编号：" + nodeNum);
    }

}
