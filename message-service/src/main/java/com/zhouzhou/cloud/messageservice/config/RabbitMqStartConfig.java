package com.zhouzhou.cloud.messageservice.config;

import jakarta.annotation.Resource;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqStartConfig {

    @Resource
    private AmqpAdmin amqpAdmin;

    private List<ExchangeConfig> exchanges;

    private List<QueueConfig> queues;

    private List<BindingConfig> bindings;

    // 定义 ExchangeConfig 类
    public static class ExchangeConfig {
        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    // 定义 QueueConfig 类
    public static class QueueConfig {
        private String name;
        private boolean durable;
        private String deadLetterExchange; // 死信交换机
        private String deadLetterRoutingKey; // 死信路由键

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDurable() {
            return durable;
        }

        public void setDurable(boolean durable) {
            this.durable = durable;
        }

        public String getDeadLetterExchange() {
            return deadLetterExchange;
        }

        public void setDeadLetterExchange(String deadLetterExchange) {
            this.deadLetterExchange = deadLetterExchange;
        }

        public String getDeadLetterRoutingKey() {
            return deadLetterRoutingKey;
        }

        public void setDeadLetterRoutingKey(String deadLetterRoutingKey) {
            this.deadLetterRoutingKey = deadLetterRoutingKey;
        }
    }


    // 定义 BindingConfig 类
    public static class BindingConfig {
        private String exchange;
        private String queue;
        private String routingKey;

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        public String getQueue() {
            return queue;
        }

        public void setQueue(String queue) {
            this.queue = queue;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public void setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
        }
    }

    // 注入和配置多个队列、交换机、绑定
    @Bean
    public List<Exchange> exchanges() {
        List<Exchange> exchangeList = new ArrayList<>();
        for (ExchangeConfig exchangeConfig : exchanges) {
            if ("direct".equalsIgnoreCase(exchangeConfig.getType())) {
                exchangeList.add(new DirectExchange(exchangeConfig.getName()));
            }

            if ("topic".equalsIgnoreCase(exchangeConfig.getType())) {
                exchangeList.add(new TopicExchange(exchangeConfig.getName()));
            }
            // 可以根据类型扩展更多的交换机类型
        }
        return exchangeList;
    }

    @Bean
    public List<Queue> queues() {
        List<Queue> queueList = new ArrayList<>();
        for (QueueConfig queueConfig : queues) {
            QueueBuilder queueBuilder = QueueBuilder.durable(queueConfig.getName());

            // 配置死信交换机和死信路由键
            if (queueConfig.getDeadLetterExchange() != null) {
                queueBuilder.deadLetterExchange(queueConfig.getDeadLetterExchange())
                        .deadLetterRoutingKey(queueConfig.getDeadLetterRoutingKey());
            }

            queueList.add(queueBuilder.build());
        }
        return queueList;
    }


    @Bean
    public List<Binding> bindings(List<Queue> queues, List<Exchange> exchanges) {
        List<Binding> bindingList = new ArrayList<>();
        for (BindingConfig bindingConfig : bindings) {
            Exchange exchange = exchanges.stream()
                    .filter(e -> e.getName().equals(bindingConfig.getExchange()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Exchange not found: " + bindingConfig.getExchange()));

            Queue queue = queues.stream()
                    .filter(q -> q.getName().equals(bindingConfig.getQueue()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Queue not found: " + bindingConfig.getQueue()));

            bindingList.add(BindingBuilder.bind(queue).to(exchange).with(bindingConfig.getRoutingKey()).noargs());
        }
        return bindingList;
    }

    /**
     * 注册交换机、队列、绑定
     */
    @Bean
    public void registerRabbitComponents() {

        // 删除所有队列（可选）
        if (queues != null) {
            for (QueueConfig queueConfig : queues) {
                amqpAdmin.deleteQueue(queueConfig.getName());
            }
        }

        // 声明交换机
        for (ExchangeConfig exchangeConfig : exchanges) {
            Exchange exchange;
            if ("direct".equalsIgnoreCase(exchangeConfig.getType())) {
                exchange = new DirectExchange(exchangeConfig.getName(), true, false);
            } else if ("topic".equalsIgnoreCase(exchangeConfig.getType())) {
                exchange = new TopicExchange(exchangeConfig.getName(), true, false);
            } else {
                throw new IllegalArgumentException("Unsupported exchange type: " + exchangeConfig.getType());
            }
            amqpAdmin.declareExchange(exchange);
        }

        // 声明队列并配置死信队列
        for (QueueConfig queueConfig : queues) {
            QueueBuilder queueBuilder = QueueBuilder.durable(queueConfig.getName());

            // 配置死信交换机和死信路由键
            if (queueConfig.getDeadLetterExchange() != null) {
                queueBuilder.deadLetterExchange(queueConfig.getDeadLetterExchange())
                        .deadLetterRoutingKey(queueConfig.getDeadLetterRoutingKey());
            }

            Queue queue = queueBuilder.build();
            amqpAdmin.declareQueue(queue);
        }

        // 声明绑定关系
        for (BindingConfig bindingConfig : bindings) {
            Exchange exchange = new DirectExchange(bindingConfig.getExchange());
            Queue queue = new Queue(bindingConfig.getQueue(), true);
            Binding binding = BindingBuilder.bind(queue).to(exchange).with(bindingConfig.getRoutingKey()).noargs();
            amqpAdmin.declareBinding(binding);
        }
    }


    // 获取配置数据
    public void setExchanges(List<ExchangeConfig> exchanges) {
        this.exchanges = exchanges;
    }

    public void setQueues(List<QueueConfig> queues) {
        this.queues = queues;
    }

    public void setBindings(List<BindingConfig> bindings) {
        this.bindings = bindings;
    }
}
