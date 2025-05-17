package com.zhouzhou.cloud;

import com.zhouzhou.cloud.websocketservice.config.RabbitMqStartConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(RabbitMqStartConfig.class)
public class WebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }
}