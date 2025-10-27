package com.zhouzhou.cloud.payservice.config;

import com.zhouzhou.cloud.payservice.strategy.PaymentStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class StrategyConfig {

    /**
     * 策略Bean会自动被Spring注入到Map中
     * key为bean名称，value为策略实例
     */
    @Bean
    public Map<String, PaymentStrategy> strategyMap() {
        return null;
    }
}
