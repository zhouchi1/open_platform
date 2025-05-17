package com.zhouzhou.cloud;


import com.zhouzhou.cloud.gatewayservice.config.RabbitMqStartConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(RabbitMqStartConfig.class)
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
        log.info("Application is running!");
    }
}
