package com.zhouzhou.cloud;


import com.zhouzhou.cloud.gatewayservice.config.RabbitMqStartConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@Slf4j
@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(RabbitMqStartConfig.class)
public class GatewayServiceApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx =
                SpringApplication.run(GatewayServiceApplication.class, args);

        log.info("Application is running!");

        log.info("====== Gateway GlobalFilters ======");
        Map<String, GlobalFilter> beans = ctx.getBeansOfType(GlobalFilter.class);
        beans.forEach((k, v) ->
                log.info("{} -> {}", k, v.getClass().getName())
        );
    }
}