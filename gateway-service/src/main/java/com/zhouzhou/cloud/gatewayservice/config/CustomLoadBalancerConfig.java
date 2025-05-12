package com.zhouzhou.cloud.gatewayservice.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-10
 * @Description: 自定义负载均衡websocket连接配置类
 */
public class CustomLoadBalancerConfig {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> customLoadBalancer(
            Environment env,
            LoadBalancerClientFactory factory) {

        String serviceId = factory.getName(env);

        ObjectProvider<ServiceInstanceListSupplier> delegate = factory.getLazyProvider(
                serviceId, ServiceInstanceListSupplier.class
        );

        return new RoundRobinLoadBalancer(
                new WeightedSupplierProvider(delegate), serviceId, -1
        );
    }

    static class WeightedSupplierProvider implements ObjectProvider<ServiceInstanceListSupplier> {

        private final ObjectProvider<ServiceInstanceListSupplier> delegate;

        public WeightedSupplierProvider(ObjectProvider<ServiceInstanceListSupplier> delegate) {
            this.delegate = delegate;
        }

        @Override
        public ServiceInstanceListSupplier getObject() {
            return new MetadataPortWeightedRule(delegate.getObject());
        }

        @Override
        public ServiceInstanceListSupplier getObject(Object... args) throws BeansException {
            return null;
        }

        @Override
        public ServiceInstanceListSupplier getIfAvailable() {
            return new MetadataPortWeightedRule(delegate.getIfAvailable());
        }

        @Override
        public ServiceInstanceListSupplier getIfUnique() {
            return new MetadataPortWeightedRule(delegate.getIfUnique());
        }
    }

    static class MetadataPortWeightedRule implements ServiceInstanceListSupplier {

        private final ServiceInstanceListSupplier delegate;

        public MetadataPortWeightedRule(ServiceInstanceListSupplier delegate) {
            this.delegate = delegate;
        }

        @Override
        public Flux<List<ServiceInstance>> get() {
            return delegate.get()
                    .map(instances -> instances.stream()
                            .map(instance -> {
                                String nettyPort = instance.getMetadata().get("netty-port");
                                if (nettyPort != null) {
                                    return new DefaultServiceInstance(
                                            instance.getInstanceId(),
                                            instance.getServiceId(),
                                            instance.getHost(),
                                            Integer.parseInt(nettyPort),
                                            instance.isSecure(),
                                            instance.getMetadata()
                                    );
                                }
                                return instance;
                            })
                            .collect(Collectors.toList())
                    );
        }

        @Override
        public String getServiceId() {
            return delegate.getServiceId();
        }
    }
}
