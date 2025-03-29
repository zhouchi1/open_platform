package com.zhouzhou.cloud.websocketservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "websocket.node")
@RefreshScope
@Getter
@Setter
public class NodeConfig {

    private List<Integer> numbers;
}
