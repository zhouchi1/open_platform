package com.zhouzhou.cloud.messageservice.rabbitmqconsumer;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import com.zhouzhou.cloud.messageservice.rabbitmqconsumer.process.ChatMessageConfirmProcess;
import com.zhouzhou.cloud.messageservice.rabbitmqconsumer.process.ChatMessagePersistenceProcess;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMessageConsumer {

    @Resource
    private ChatMessageConfirmProcess chatMessageConfirmProcess;

    @Resource
    private ChatMessagePersistenceProcess chatMessagePersistenceProcess;

    /**
     * 布隆过滤器
     */
    public static BitMapBloomFilter bloomFilter = new BitMapBloomFilter(100);

    @RabbitListener(queues = "topicQueue1")
    public void receiveMessageTopicQueue2(Message message) {

        String messageId = message.getMessageProperties().getMessageId();

        if (messageId == null) {
            log.warn("接收到没有 messageId 的消息，跳过处理！");
            return;
        }

        // 判断是否是重复消息
        if (bloomFilter.contains(messageId)) {
            log.warn("messageId:【" + messageId + "】消息重复消费，进行过滤!");
            return;
        }

        // 第一次处理该消息，加入布隆过滤器
        bloomFilter.add(messageId);

        try {
            chatMessagePersistenceProcess.processMessage(message);
        } catch (Exception e) {
            log.error("消息处理异常: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "topicQueue2")
    public void receiveMessageTopicQueue1(Message message) {

        String messageId = message.getMessageProperties().getMessageId();

        if (messageId == null) {
            log.warn("接收到没有 messageId 的消息，跳过处理！");
            return;
        }

        // 判断是否是重复消息
        if (bloomFilter.contains(messageId)) {
            log.warn("messageId:【" + messageId + "】消息重复消费，进行过滤!");
            return;
        }

        // 第一次处理该消息，加入布隆过滤器
        bloomFilter.add(messageId);

        try {
            chatMessageConfirmProcess.processMessage(message);
        } catch (Exception e) {
            log.error("消息处理异常: " + e.getMessage());
        }
    }
}

