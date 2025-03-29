package com.zhouzhou.cloud.websocketservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "websocket")
public class NodeConfig {

    private Map<String,Integer> node;
}
