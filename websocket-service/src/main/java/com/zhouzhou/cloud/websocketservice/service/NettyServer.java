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
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
@DependsOn("stringRedisTemplate") // 关键注解
public class NettyServer {

    @Value("${websocket.port.nodeAPort}")
    private Integer port;

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
                start();
            } catch (InterruptedException | UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 优雅启动
     *
     * @throws InterruptedException 系统中断异常
     * @throws UnknownHostException 系统未知异常
     */
    public void start() throws InterruptedException, UnknownHostException {
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
            log.error("警告！！！Netty-Websocket服务Cluster节点【主机/IP + 端口：" + InetAddress.getLocalHost() + ":" + port + "】已下线 通讯服务失效！");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    /**
     * 优雅下线该分布式节点
     */
    @PreDestroy
    public void onShutdown() {

//        try {
//            String hostAddress = InetAddress.getLocalHost().getHostAddress();
//            log.info("分布式节点已下线，节点信息：【{}:{}】", hostAddress, port);
//            String nodeKey = hostAddress + ":" + port;
//
//            if (stringRedisTemplate != null) {
//                stringRedisTemplate.delete(NODE_CHANNEL_USER_INFO + nodeKey);
//                stringRedisTemplate.opsForSet().remove(WS_NODE_STATUS, nodeKey);
//                log.info("缓存清理完成");
//            } else {
//                log.error("stringRedisTemplate 未注入");
//            }
//        } catch (UnknownHostException e) {
//            log.error("获取主机地址失败", e);
//        } catch (Exception e) {
//            log.error("关闭时发生异常", e);
//        }
    }

    public void registerNodeHeartbeat() throws UnknownHostException {

        String nodeKey = InetAddress.getLocalHost().getHostAddress() + ":" + port;

        stringRedisTemplate.opsForSet().add(WS_NODE_STATUS, nodeKey);

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> stringRedisTemplate.expire(WS_NODE_STATUS, 30, TimeUnit.SECONDS), 0, 5, TimeUnit.SECONDS);

        log.info("分布式节点已注册到Redis中，节点信息：【" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "】");
    }

}
