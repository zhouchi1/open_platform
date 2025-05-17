package com.zhouzhou.cloud.messageservice.rabbitmqconsumer.strategy;

import com.zhouzhou.cloud.messageservice.rabbitmqconsumer.MessageProcessingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component("ChatMessagePersistenceStrategy")
public class ChatMessagePersistenceStrategy implements MessageProcessingStrategy {

    @Override
    public void processMessage(Message message) throws Exception {

        // 解析Message

    }
}
