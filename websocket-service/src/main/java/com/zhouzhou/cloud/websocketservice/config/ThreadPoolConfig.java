package com.zhouzhou.cloud.websocketservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;


@EnableAsync
@Configuration
public class ThreadPoolConfig {

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

        // 关键配置：等待所有任务完成后再关闭
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置等待终止的最大时长
        executor.setAwaitTerminationSeconds(60);

        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("simple-executor-");

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 执行初始化
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