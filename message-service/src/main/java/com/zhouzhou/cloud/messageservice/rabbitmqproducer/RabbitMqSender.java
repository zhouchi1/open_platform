package com.zhouzhou.cloud.messageservice.rabbitmqproducer;

import com.zhouzhou.cloud.common.service.interfaces.RabbitMqSenderApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

@Slf4j
@DubboService(version = "1.0.0")
public class RabbitMqSender implements RabbitMqSenderApi {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 简单消息发送
     *
     * @param queueName 队列名称
     * @param message   消息内容
     */
    public void sendSimpleMessage(String queueName, Object message) {
        try {
            rabbitTemplate.convertAndSend(queueName, message, msg -> {
                // 生成每条消息的 traceId 用于消息追踪
                msg.getMessageProperties().setHeader("traceId", UUID.randomUUID().toString());
                return msg;
            });

            log.info("Simple message sent to queue: " + queueName);
        } catch (Exception e) {
            log.error("Failed to send simple message: " + e.getMessage());
        }
    }

    /**
     * 路由消息发送
     *
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     * @param message      消息内容
     */
    @Override
    public void sendRoutingMessage(String exchangeName, String routingKey, Object message) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message, msg -> {
                msg.getMessageProperties().setHeader("traceId", UUID.randomUUID().toString());
                return msg;
            });
           log.info("Routing message sent to exchange: " + exchangeName + " with routing key: " + routingKey);
        } catch (Exception e) {
            log.error("Failed to send routing message: " + e.getMessage());
        }
    }

    /**
     * 广播消息发送
     *
     * @param exchangeName 交换机名称（Fanout 类型）
     * @param message      消息内容
     */
    public void sendBroadcastMessage(String exchangeName, Object message) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, "", message, msg -> {
                msg.getMessageProperties().setHeader("traceId", UUID.randomUUID().toString());
                return msg;
            });
            System.out.println("Broadcast message sent to exchange: " + exchangeName);
        } catch (Exception e) {
            System.err.println("Failed to send broadcast message: " + e.getMessage());
        }
    }

    /**
     * 延迟消息发送
     *
     * @param exchangeName 交换机名称（Direct 或 Topic 类型）
     * @param routingKey   路由键
     * @param message      消息内容
     * @param delay        延迟时间（毫秒）
     */
    public void sendDelayedMessage(String exchangeName, String routingKey, Object message, int delay) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message, msg -> {
                msg.getMessageProperties().setHeader("traceId", UUID.randomUUID().toString());
                msg.getMessageProperties().setDelay(delay);
                return msg;
            });
            System.out.println("Delayed message sent to exchange: " + exchangeName + " with delay: " + delay + "ms");
        } catch (Exception e) {
            System.err.println("Failed to send delayed message: " + e.getMessage());
        }
    }

    /**
     * 发送消息到 Topic 交换机
     *
     * @param exchangeName 交换机名称（Topic 类型）
     * @param routingKey   路由键
     * @param message      消息内容
     */
    public void sendTopicMessage(String exchangeName, String routingKey, Object message) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message, msg -> {
                msg.getMessageProperties().setHeader("traceId", UUID.randomUUID().toString());
                return msg;
            });
            System.out.println("Topic message sent to exchange: " + exchangeName + " with routing key: " + routingKey);
        } catch (Exception e) {
            System.err.println("Failed to send topic message: " + e.getMessage());
        }
    }
}
