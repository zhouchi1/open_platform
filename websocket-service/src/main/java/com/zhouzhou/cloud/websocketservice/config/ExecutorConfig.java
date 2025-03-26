package com.zhouzhou.cloud.websocketservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 线程池配置
 */
@EnableAsync
@Configuration
public class ExecutorConfig {

    public static final String MY_EXECUTOR = "MyExecutor";

    @Bean(MY_EXECUTOR)
    public ThreadPoolTaskExecutor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(16);
        // 配置最大线程数
        executor.setMaxPoolSize(32);

        // 配置队列大小
        executor.setQueueCapacity(50000);

        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("my-open-platform-executor-");

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 执行初始化
        executor.initialize();

        return executor;
    }
}
