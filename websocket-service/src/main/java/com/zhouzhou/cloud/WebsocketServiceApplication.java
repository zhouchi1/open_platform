package com.zhouzhou.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class WebsocketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketServiceApplication.class, args);
    }
}
