package com.zhouzhou.cloud.warehouseservice.rabbitconsumer;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @RabbitListener(queues = "queue1")
    public void receiveMessageFromQueueOne(Message message) {
        String traceId = (String) message.getMessageProperties().getHeaders().get("traceId");
        String messageBody = JSON.parseObject(message.getBody(), String.class);
        System.out.println("Received message from queue1: " + messageBody);
    }

    @RabbitListener(queues = "queue2")
    public void receiveMessageFromQueueTwo(Message  message) {
        String traceId = (String) message.getMessageProperties().getHeaders().get("traceId");
        System.out.println("Received message from queue2: " + message);
    }

    @RabbitListener(queues = "queue3")
    public void receiveMessageFromQueueThree(Message  message) {
        String traceId = (String) message.getMessageProperties().getHeaders().get("traceId");
        System.out.println("Received message from queue3: " + message);
    }
}
