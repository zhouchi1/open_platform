package com.zhouzhou.cloud.websocketservice.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;
import static com.zhouzhou.cloud.websocketservice.constant.ZookeeperLockConstants.*;

@Slf4j
@Component
@RefreshScope
public class CleanConnectJob {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CuratorFramework curatorFramework;

    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanAllConnect(){
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, ALL_CHANNEL_CLEAN_LOCK_PATH);
        try {
            if (lock.acquire(300, TimeUnit.SECONDS)) {
                try {
                    log.warn("Zookeeper分布式锁获取成功 开始执行【清理所有节点】连接任务。");
                    stringRedisTemplate.opsForHash().delete(NODE_CHANNEL_USER_INFO);
                } finally {
                    lock.release();
                }
            } else {
                log.warn("未能获取到Zookeeper分布式锁，跳过本次清理所有节点连接任务。");
            }
        } catch (Exception e) {
            log.error("获取或释放Zookeeper【" + ALL_CHANNEL_CLEAN_LOCK_PATH + "】分布式锁异常：", e);
        }
    }
}
