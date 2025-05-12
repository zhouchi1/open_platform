package com.zhouzhou.cloud.gatewayservice.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-10
 * @Description: Nacos初始化配置
 */
@Configuration
public class NacosConfig {

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddr;

    @Value("${spring.cloud.nacos.discovery.namespace:}")
    private String namespace;

    @Value("${spring.cloud.nacos.discovery.username:}")
    private String username;

    @Value("${spring.cloud.nacos.discovery.password:}")
    private String password;

    @Bean
    public NamingService namingService() throws NacosException {
        Properties props = new Properties();
        props.put("serverAddr", serverAddr);
        if (!namespace.isEmpty()) {
            props.put("namespace", namespace);
        }
        if (!username.isEmpty()) {
            props.put("username", username);
            props.put("password", password);
        }
        return NacosFactory.createNamingService(props);
    }
}

