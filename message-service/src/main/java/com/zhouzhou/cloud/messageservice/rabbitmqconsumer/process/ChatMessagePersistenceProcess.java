package com.zhouzhou.cloud.messageservice.rabbitmqconsumer.process;

import com.alibaba.fastjson2.JSONObject;
import com.zhouzhou.cloud.common.dto.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatMessagePersistenceProcess {

    public void processMessage(Message message) throws Exception {

        String deserializeMessage = new String(message.getBody());
        MessageDTO messageDTO = JSONObject.parseObject(deserializeMessage, MessageDTO.class);
        log.info("消息持久化成功，消息Id：{}", messageDTO.getMessageId());

    }
}
