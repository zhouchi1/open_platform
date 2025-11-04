package com.zhouzhou.cloud.authservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfigChecker implements ApplicationRunner {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== 配置检查 ===");
        log.info("数据库 URL: " + datasourceUrl);
        log.info("=== 配置检查结束 ===");
    }
}
