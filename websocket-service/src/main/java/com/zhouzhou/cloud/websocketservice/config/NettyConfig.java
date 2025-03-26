package com.zhouzhou.cloud.websocketservice.config;

import com.zhouzhou.cloud.websocketservice.hanlder.WebSocketHandler;
import io.netty.channel.group.ChannelGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 通道组配置
 */
@Configuration
public class NettyConfig {

    @Bean
    public ChannelGroup webSocketChannels() {
        return WebSocketHandler.channels;
    }
}
