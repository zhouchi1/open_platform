package com.zhouzhou.cloud.messageservice.rabbitmqconsumer;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMessageConsumer {

    @Resource
    private MessageStrategyContext strategyContext;

    /**
     * 布隆过滤器
     */
    public static BitMapBloomFilter bloomFilter = new BitMapBloomFilter(100);


    @RabbitListener(queues = "topicQueue1")
    public void receiveMessage(Message message) {

        // 在RabbitMQ中，消息ID存放在messageProperties中
        String messageId = message.getMessageProperties().getMessageId();

        // 布隆过滤器进行消息去重过滤
        if (!bloomFilter.contains(messageId)) {
            log.warn("messageId:【" + messageId + "】消息重复消费，以进行过滤!");
        }

        try {
            strategyContext.execute(message);
        } catch (Exception e) {
            System.err.println("消息处理异常: " + e.getMessage());
        }
    }
}

