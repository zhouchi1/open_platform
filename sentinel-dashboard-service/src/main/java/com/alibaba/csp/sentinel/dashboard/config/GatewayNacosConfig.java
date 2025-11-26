package com.alibaba.csp.sentinel.dashboard.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.NacosConfigUtil;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class GatewayNacosConfig {

    @Autowired
    private ConfigService configService;

    @Bean
    public Converter<List<GatewayFlowRule>, String> gatewayFlowRuleEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<GatewayFlowRule>> gatewayFlowRuleDecoder() {
        return s -> JSON.parseArray(s, GatewayFlowRule.class);
    }

    @Bean
    public DynamicRuleProvider<List<GatewayFlowRule>> gatewayFlowRuleNacosProvider(
            Converter<String, List<GatewayFlowRule>> converter) {
        return app -> {
            String rules = configService.getConfig(app + "-gw-flow-rules",
                    NacosConfigUtil.GROUP_ID, 3000);
            if (StringUtil.isEmpty(rules)) {
                return new ArrayList<>();
            }
            return converter.convert(rules);
        };
    }

    @Bean
    public DynamicRulePublisher<List<GatewayFlowRule>> gatewayFlowRuleNacosPublisher(
            Converter<List<GatewayFlowRule>, String> converter) {
        return (app, rules) -> {
            AssertUtil.notEmpty(app, "app name cannot be empty");
            if (rules == null) {
                return;
            }
            configService.publishConfig(app + "-gw-flow-rules",
                    NacosConfigUtil.GROUP_ID, converter.convert(rules));
        };
    }

    // API 分组规则的持久化
    @Bean
    public Converter<List<ApiDefinition>, String> gatewayApiEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<ApiDefinition>> gatewayApiDecoder() {
        return s -> JSON.parseArray(s, ApiDefinition.class);
    }

    @Bean
    public DynamicRuleProvider<List<ApiDefinition>> gatewayApiNacosProvider(
            Converter<String, List<ApiDefinition>> converter) {
        return app -> {
            String rules = configService.getConfig(app + "-gw-api-group",
                    NacosConfigUtil.GROUP_ID, 3000);
            if (StringUtil.isEmpty(rules)) {
                return new ArrayList<>();
            }
            return converter.convert(rules);
        };
    }

    @Bean
    public DynamicRulePublisher<List<ApiDefinition>> gatewayApiNacosPublisher(
            Converter<List<ApiDefinition>, String> converter) {
        return (app, rules) -> {
            AssertUtil.notEmpty(app, "app name cannot be empty");
            if (rules == null) {
                return;
            }
            configService.publishConfig(app + "-gw-api-group",
                    NacosConfigUtil.GROUP_ID, converter.convert(rules));
        };
    }
}
