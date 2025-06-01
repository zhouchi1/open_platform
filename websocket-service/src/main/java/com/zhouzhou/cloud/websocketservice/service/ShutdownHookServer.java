package com.zhouzhou.cloud.websocketservice.service;

import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import io.netty.channel.Channel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-29
 * @Description: 服务关闭 用户在线信息处理钩子
 */
@Slf4j
@RefreshScope
@Component
public class ShutdownHookServer {

    @Resource
    private RedisUtil redisUtil;

    @PostConstruct
    public void registerHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // 删除所有连接此节点的用户的在线状态
                Map<Channel, String> channelUserMap =  ChannelConfig.getChannelUserMap();
                for (Map.Entry<Channel, String> entry : channelUserMap.entrySet()) {
                    Channel channel = entry.getKey();
                    String userPlatformUniqueInfo = entry.getValue();
                    try {
                        redisUtil.delete(userPlatformUniqueInfo + "status");
                    } finally {
                        channel.close();
                    }
                }
                log.info("ShutdownHook completed successfully");
            } catch (Exception e) {
                log.error("ShutdownHook failed to clear the mapping relationship between users and servers", e);
            }
        }));
    }
}
