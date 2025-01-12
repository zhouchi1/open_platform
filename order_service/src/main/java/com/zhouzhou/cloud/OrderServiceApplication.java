package com.zhouzhou.cloud;

import com.zhouzhou.cloud.common.config.RabbitMqStartConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableRabbit
@EnableDubbo
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.zhouzhou.cloud.orderservice.mapper")
@EnableConfigurationProperties(RabbitMqStartConfig.class)
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
