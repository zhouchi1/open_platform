package com.zhouzhou.cloud.websocketservice.config;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 通道配置
 */
@Component
public class ChannelConfig {

    private static final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Channel> getChannelMap() {
        return channelMap;
    }

    public static void addChannel(String userId, Channel channel) {
        channelMap.put(userId, channel);
    }

    public static void removeChannel(String userId) {
        channelMap.remove(userId);
    }

    public static Channel getChannel(String userId) {
        return ObjectUtils.isEmpty(channelMap.get(userId)) ? null : channelMap.get(userId);
    }
}
