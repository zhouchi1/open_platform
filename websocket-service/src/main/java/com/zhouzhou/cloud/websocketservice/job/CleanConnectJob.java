package com.zhouzhou.cloud.websocketservice.job;

import cn.hutool.core.collection.CollectionUtil;
import com.zhouzhou.cloud.common.utils.RedisLockUtil;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import io.netty.channel.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-04-05
 * @Description: 相关定时任务处理
 */
@Slf4j
@Component
public class CleanConnectJob {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisLockUtil redisLockUtil;

    @Value("${websocket.port}")
    private Integer port;


    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanAllConnect() {

        String key = REDIS_CLEAN_ALL_CONNECT_KEY + LocalDate.now().format(DATE_FORMATTER);
        long currentTime = Instant.now().plus(20, MINUTES).toEpochMilli();

        boolean lock = redisLockUtil.lock(key, String.valueOf(currentTime));

        if (!lock) {
            log.warn("未能获取到Redis分布式锁，跳过本次清理所有节点连接任务。");
            return;
        }
        log.info("{} 锁定成功，开始处理业务", key);

        try {
            log.info("Redis分布式锁获取成功 开始执行【清理所有节点】连接任务。");

            ConcurrentHashMap<String, Channel> channelMap = ChannelConfig.getChannelMap();

            Collection<Channel> channels = channelMap.values();

            List<Channel> channelList = new ArrayList<>(channels);
            channelMap.clear();

            for (Channel channel : channelList) {
                try {
                    if (channel != null && channel.isActive()) {
                        channel.close().sync();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("关闭所有连接时发生了中断异常");
                } catch (Exception e) {
                    log.error("关闭所有连接通道异常：", e);
                }
            }

            stringRedisTemplate.delete(NODE_CHANNEL_USER_INFO + InetAddress.getLocalHost().getHostAddress() + ":" + port);
            log.info("{} 解锁成功，结束处理业务", key);
        } catch (Exception e) {
            log.error("获取或释放Redis【" + REDIS_CLEAN_ALL_CONNECT_KEY + "】分布式锁异常：", e);
        } finally {
            redisLockUtil.unlock(key, String.valueOf(currentTime));
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanAllUnlessMessage() {
        String key = REDIS_CLEAN_ALL_UNLESS_MESSAGE_KEY + LocalDate.now().format(DATE_FORMATTER);
        long currentTime = Instant.now().plus(20, MINUTES).toEpochMilli();
        boolean lock = redisLockUtil.lock(key, String.valueOf(currentTime));

        if (!lock) {
            log.warn("未能获取到Redis分布式锁，跳过本次【清理3天过期消息】任务。");
            return;
        }
        log.info("{} 锁定成功，开始处理业务", key);

        try {
            log.info("Redis分布式锁获取成功 开始执行【清理3天过期消息】连接任务。");

            long now = System.currentTimeMillis();
            Set<String> expired = stringRedisTemplate.opsForZSet().rangeByScore(OFFLINE_MESSAGE_BY_USER_EXPIRE, 0, now);

            if (!CollectionUtil.isEmpty(expired)) {
                expired.forEach(messageId -> {
                    stringRedisTemplate.opsForZSet().remove(OFFLINE_MESSAGE_BY_USER_EXPIRE, messageId);
                    String userId = String.valueOf(stringRedisTemplate.opsForValue().get(messageId));
                    stringRedisTemplate.opsForHash().delete(OFFLINE_MESSAGE_BY_USER + userId, messageId);
                    stringRedisTemplate.delete(messageId);
                });
            }

            log.info("{} 解锁成功，结束处理业务", key);
        } catch (Exception e) {
            log.error("获取或释放Redis【" + REDIS_CLEAN_ALL_CONNECT_KEY + "】分布式锁异常：", e);
        } finally {
            redisLockUtil.unlock(key, String.valueOf(currentTime));
        }
    }
}
