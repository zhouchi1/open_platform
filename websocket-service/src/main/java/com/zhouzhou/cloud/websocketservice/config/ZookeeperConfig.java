package com.zhouzhou.cloud.websocketservice.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    // 从配置文件读取 ZooKeeper 连接地址
    @Value("${zookeeper.connection-string}")
    private String connectionString;

    @Bean
    public CuratorFramework curatorFramework() {
        // 创建 Curator 客户端，设置重试策略
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        // 启动客户端
        client.start();
        return client;
    }
}
