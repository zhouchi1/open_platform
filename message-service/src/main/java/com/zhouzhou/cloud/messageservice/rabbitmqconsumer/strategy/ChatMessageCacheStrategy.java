package com.zhouzhou.cloud.messageservice.rabbitmqconsumer.strategy;

import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.messageservice.rabbitmqconsumer.MessageProcessingStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component("ChatMessageCacheStrategy")
public class ChatMessageCacheStrategy implements MessageProcessingStrategy {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void processMessage(Message message) throws Exception {

    }
}
