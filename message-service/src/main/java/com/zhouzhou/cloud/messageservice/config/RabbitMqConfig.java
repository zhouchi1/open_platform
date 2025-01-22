package com.zhouzhou.cloud.messageservice.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.retry.support.RetryTemplate;


@Configuration
public class RabbitMqConfig {


    /**
     * 配置 RabbitTemplate，包含重试机制和异常处理
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 配置重试模板
        RetryTemplate retryTemplate = new RetryTemplateBuilder()
                .fixedBackoff(1000) // 重试间隔时间
                .maxAttempts(3)     // 最大重试次数
                .build();

        rabbitTemplate.setRetryTemplate(retryTemplate);

        // 设置异常处理，重试多次后仍失败时触发
        rabbitTemplate.setRecoveryCallback(context -> {
            Throwable cause = context.getLastThrowable();
            // 发送给死信队列
            return null;
        });

        // 设置 ConfirmCallback，用于监控消息是否成功到达交换机
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("Message confirmed successfully.");
            } else {
                // 如果消息没有到达交换机 则将消息发送给死信队列
                System.err.println("Message confirmation failed: " + cause);
            }
        });

        // 设置 ReturnCallback，用于监控消息是否路由失败
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {

            // 发送给死信队列
            System.err.println("Message returned: " + replyText);
        });

        return rabbitTemplate;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动确认模式
        return factory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}


