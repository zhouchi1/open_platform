package com.zhouzhou.cloud.warehouseservice.rabbitconsumer;

import com.rabbitmq.client.Channel;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MessageConsumer {

    @Resource
    private RedisUtil redisUtil;


    @RabbitListener(queues = "queue1")
    public void receiveMessageFromQueueOne(Message message, Channel channel) throws IOException {
        try {
            String traceId = (String) message.getMessageProperties().getHeaders().get("traceId");
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received message from queue1: " + messageBody);

            // 检查消息是否已经处理过
            if (redisUtil.hasKey("processed:" + traceId)) {
                System.out.println("Message already processed: " + traceId);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }

            // 缓存该消息Id
            redisUtil.set("processed:" + traceId, "true",-1);

            // 处理消息逻辑


            // 确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 拒绝消息并重新入队
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "queue2")
    public void receiveMessageFromQueueTwo(Message message, Channel channel) throws IOException {
        try {
            String traceId = (String) message.getMessageProperties().getHeaders().get("traceId");
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received message from queue1: " + messageBody);

            // 检查消息是否已经处理过
            if (redisUtil.hasKey("processed:" + traceId)) {
                System.out.println("Message already processed: " + traceId);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }

            // 缓存该消息Id
            redisUtil.set("processed:" + traceId, "true",-1);

            // 处理消息逻辑


            // 确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 拒绝消息并重新入队
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "queue3")
    public void receiveMessageFromQueueThree(Message message, Channel channel) throws IOException {
        try {
            String traceId = (String) message.getMessageProperties().getHeaders().get("traceId");
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received message from queue1: " + messageBody);

            // 检查消息是否已经处理过
            if (redisUtil.hasKey("processed:" + traceId)) {
                System.out.println("Message already processed: " + traceId);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }

            // 缓存该消息Id
            redisUtil.set("processed:" + traceId, "true",-1);

            // 处理消息逻辑


            // 确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 拒绝消息并重新入队
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }
}
