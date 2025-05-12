package com.zhouzhou.cloud.gatewayservice.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-10
 * @Description: 识别自定义负载均衡策略
 */
@Configuration
@LoadBalancerClients(defaultConfiguration = CustomLoadBalancerConfig.class)
public class LoadBalancerAutoConfig {

}