package com.zhouzhou.cloud.websocketservice.job;

import com.zhouzhou.cloud.websocketservice.config.NodeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.CHANNEL_ID;
import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.NODE_ID;

@Slf4j
@Component
@RefreshScope
public class CleanConnectJob {

    @Resource
    private NodeConfig nodeConfig;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void cleanConnect() {
        for (Integer nodeNum : nodeConfig.getNumbers()){
            String nodeKey = "ws:node:status:" + nodeNum;
            Boolean exists = stringRedisTemplate.hasKey(nodeKey);
            if (Boolean.FALSE.equals(exists)) {
                stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys(NODE_ID + nodeNum + CHANNEL_ID + "*")));
            }
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
