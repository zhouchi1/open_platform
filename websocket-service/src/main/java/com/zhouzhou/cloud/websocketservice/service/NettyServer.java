package com.zhouzhou.cloud.websocketservice.service;

import com.zhouzhou.cloud.websocketservice.handler.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.FastThreadLocal;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

@Slf4j
@Component
@DependsOn("stringRedisTemplate")
public class NettyServer {

    @Value("${websocket.port}")
    private Integer port;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    // 保存主通道引用，用于优雅关闭
    private ChannelFuture serverChannelFuture;

    // 心跳任务控制器
    private ScheduledFuture<?> heartbeatFuture;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ThreadPoolTaskExecutor myExecutor;

    @Resource
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Resource
    private WebSocketChannelInitializer webSocketChannelInitializer;

    // 移除构造函数中的初始化
    public NettyServer() {

    }

    @PostConstruct
    public void init() {

        // 开启内存泄漏检测
        System.setProperty("io.netty.leakDetection.level", "PARANOID");
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);

        new Thread(() -> {
            try {
                startChannelGroup();
            } catch (Exception e) {
                log.error("Netty服务启动失败", e);
            }
        }, "Netty-Starter-Thread").start();
    }

    public void startChannelGroup() {
        // 初始化线程组
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-boss", true) {
            @Override
            protected Thread newThread(Runnable r, String name) {
                return new Thread(r, name) {
                    @Override
                    public void run() {
                        super.run();
                    }
                };
            }
        });

        workerGroup = new NioEventLoopGroup(32, new DefaultThreadFactory("netty-worker", true) {
            @Override
            protected Thread newThread(Runnable r, String name) {
                return new Thread(r, name) {
                    @Override
                    public void run() {
                        super.run();
                    }
                };
            }
        });

        ExecutorService executorService = myExecutor.getThreadPoolExecutor();
        executorService.submit(() -> {
            try {
                start();
            } catch (Exception e) {
                // 增强异常处理
                log.error("Netty服务启动失败", e);
                // 启动失败时立即清理资源
                shutdown();
            }
        });
    }


    public void start() throws Exception {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, THREAD_WAIT_NUM)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(webSocketChannelInitializer);

            // 保存通道引用
            serverChannelFuture = bootstrap.bind(port).sync();

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
                    "|  Node: " + getNodeKey() + "                                          |\n" +
                    "+-----------------------------------------------------------------+\n");

            registerNodeHeartbeat();

            // 添加关闭监听器
            serverChannelFuture.channel().closeFuture().addListener(future -> log.info("Netty服务已停止")).sync();

        } finally {
            // 仅记录错误，不释放资源（资源在shutdown方法中释放）
            log.error("Netty服务已停止，节点 {}", getNodeKey());
        }
    }

    private void registerNodeHeartbeat() {
        String nodeKey = getNodeKey();
        // 注册节点到活跃节点集合
        stringRedisTemplate.opsForSet().add(WS_NODE_STATUS, nodeKey);

        // 保存心跳任务引用
        this.heartbeatFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            try {
                // 心跳续期操作
                stringRedisTemplate.expire(WS_NODE_STATUS, 30, TimeUnit.SECONDS);
                log.debug("节点心跳续期: {}", nodeKey);
            } catch (Exception e) {
                log.error("节点心跳续期失败", e);
            }
        }, 0, 5, TimeUnit.SECONDS);

        log.info("分布式节点已注册到Redis中，节点信息：【{}】", nodeKey);
    }

    @PreDestroy
    public void shutdown() {
        log.info("================================================");
        log.info("  开始执行优雅停机流程 (Graceful Shutdown)      ");
        log.info("================================================");

        String nodeKey = getNodeKey();

        try {

            // 停止心跳任务
            if (heartbeatFuture != null) {
                heartbeatFuture.cancel(true);
                log.info("√ 心跳任务已停止");
            }

            // 关闭监听端口（不再接收新连接）
            if (serverChannelFuture != null) {
                serverChannelFuture.channel().close().sync();
                log.info("√ 服务端口已关闭");
            }

            // 清理Redis注册信息
            log.info("清理节点注册信息: {}", nodeKey);
            stringRedisTemplate.delete(NODE_CHANNEL_USER_INFO + nodeKey);
            stringRedisTemplate.opsForSet().remove(WS_NODE_STATUS, nodeKey);

            // 优雅关闭线程组
            if (workerGroup != null) {
                // 允许最多15秒关闭时间
                workerGroup.shutdownGracefully(0, 15, TimeUnit.SECONDS)
                        .addListener(future -> {
                            if (future.isSuccess()) {
                                log.info("√ Worker线程组已关闭");
                            } else {
                                log.error("Worker线程组关闭异常");
                            }
                        });
            }
            // 清理FastThreadLocal资源
            FastThreadLocal.removeAll();

            if (bossGroup != null) {
                bossGroup.shutdownGracefully(0, 5, TimeUnit.SECONDS)
                        .addListener(future -> {
                            if (future.isSuccess()) {
                                log.info("√ Boss线程组已关闭");
                            } else {
                                log.error("Boss线程组关闭异常");
                            }
                        });
            }

            // 等待所有连接关闭（根据实际情况实现）
            waitForConnectionsRelease();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("关闭过程被中断", e);
        } catch (Exception e) {
            log.error("关闭过程中发生异常", e);
        } finally {
            log.info("================================================");
            log.info("  优雅停机流程执行完成          ");
            log.info("================================================");
        }
    }

    private void waitForConnectionsRelease() {
        // 这里简化处理，等待2秒让连接处理完成
        try {
            log.info("等待现有连接处理完成...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String getNodeKey() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            log.error("获取主机地址失败", e);
            return "unknown-host:" + port;
        }
    }
}