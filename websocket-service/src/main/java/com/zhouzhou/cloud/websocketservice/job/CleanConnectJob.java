package com.zhouzhou.cloud.websocketservice.job;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RefreshScope
public class CleanConnectJob {


    @Resource
    private CuratorFramework curatorFramework;

    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanAllConnect(){

    }
}
