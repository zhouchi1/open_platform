package com.zhouzhou.cloud.websocketservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 线程池配置
 */
@EnableAsync
@Configuration
public class ExecutorConfig {

    public static final String WEBSOCKET_EXECUTOR = "Websocket_Executor";

    @Bean(WEBSOCKET_EXECUTOR)
    public ThreadPoolTaskExecutor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(16);

        executor.setMaxPoolSize(32);

        executor.setQueueCapacity(50000);

        executor.setThreadNamePrefix("my-open-platform-executor-");

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }

    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(
                4,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
