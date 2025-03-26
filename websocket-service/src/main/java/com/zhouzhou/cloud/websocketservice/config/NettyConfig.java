package com.zhouzhou.cloud.websocketservice.config;

import com.zhouzhou.cloud.websocketservice.hanlder.WebSocketHandler;
import io.netty.channel.group.ChannelGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfig {

    @Bean
    public ChannelGroup webSocketChannels() {
        return WebSocketHandler.channels;
    }
}
