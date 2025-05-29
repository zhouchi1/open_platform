package com.zhouzhou.cloud.websocketservice.config;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 通道配置
 */
@Component
public class ChannelConfig {

    /**
     * 绑定通道与平台userId
     */
    private static final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Channel> getChannelMap() {
        return channelMap;
    }

    public static void addChannel(String userPlatformUniqueInfo, Channel channel) {
        channelMap.put(userPlatformUniqueInfo, channel);
    }

    public static void removeChannel(String userPlatformUniqueInfo) {
        channelMap.remove(userPlatformUniqueInfo);
    }

    public static Channel getChannel(String userPlatformUniqueInfo) {
        return ObjectUtils.isEmpty(channelMap.get(userPlatformUniqueInfo)) ? null : channelMap.get(userPlatformUniqueInfo);
    }

    /**
     * 绑定平台userId与通道
     */
    private static final ConcurrentHashMap<Channel, String> channelUserMap = new ConcurrentHashMap<>();

    public static Map<Channel, String> getChannelUserMap() {
        return channelUserMap;
    }

    public static void bindChannelUser(Channel channel, String userPlatformUniqueInfo) {
        channelUserMap.put(channel, userPlatformUniqueInfo);
    }

    public static void removeChannelUser(Channel channel) {
        channelUserMap.remove(channel);
    }

    public static String getPlatformUserIdByChannel(Channel channel) {
        return channelUserMap.get(channel);
    }


}
