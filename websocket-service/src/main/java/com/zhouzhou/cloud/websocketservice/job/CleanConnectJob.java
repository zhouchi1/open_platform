package com.zhouzhou.cloud.websocketservice.job;

import com.zhouzhou.cloud.websocketservice.config.NodeConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

@Slf4j
@Component
@RefreshScope
public class CleanConnectJob {

    @Resource
    private NodeConfig nodeConfig;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CuratorFramework curatorFramework;

    private static final String LOCK_PATH = "/lock/cleanConnect";

    @Scheduled(cron = "0 * * * * ?")
    public void cleanConnect() {
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, LOCK_PATH);
        try {
            if (lock.acquire(300, TimeUnit.SECONDS)) {
                try {
                    log.info("Zookeeper分布式锁获取成功 开始执行清理连接任务。");
                    nodeConfig.getNode().forEach((key, value) -> {
                        String nodeKey = WS_NODE_STATUS + value;
                        Boolean exists = stringRedisTemplate.hasKey(nodeKey);
                        if (Boolean.FALSE.equals(exists)) {
                            stringRedisTemplate.delete(
                                    Objects.requireNonNull(
                                            stringRedisTemplate.keys(NODE_ID + value + CHANNEL_ID + "*")
                                    )
                            );
                        }
                    });
                } finally {
                    lock.release();
                }
            } else {
                log.warn("未能获取到Zookeeper分布式锁，跳过本次清理连接任务。");
            }
        } catch (Exception e) {
            log.error("获取或释放Zookeeper分布式锁异常：", e);
        }
    }

    /**
     * 每晚一点定时清理所有节点的连接
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanAllConnect() {
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys(NODE_ID + "*" + CHANNEL_ID + "*")));
    }
}
