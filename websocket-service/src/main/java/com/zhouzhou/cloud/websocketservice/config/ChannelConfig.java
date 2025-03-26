package com.zhouzhou.cloud.websocketservice.config;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChannelConfig {

    private static final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Channel> getChannelMap() {
        return channelMap;
    }

    public static void addChannel(String channelId, Channel channel) {
        channelMap.put(channelId, channel);
    }

    public static void removeChannel(String channelId) {
        channelMap.remove(channelId);
    }

    public static Channel getChannel(String channelId) {
        return ObjectUtils.isEmpty(channelMap.get(channelId)) ? null : channelMap.get(channelId);
    }
}
