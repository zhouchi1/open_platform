package com.zhouzhou.cloud.messageservice.rabbitmqconsumer.process;

import com.alibaba.fastjson2.JSONObject;
import com.zhouzhou.cloud.common.dto.MessageDTO;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ChatMessageCacheProcess {

    @Resource
    private RedisUtil redisUtil;

    public void processMessage(Message message) {
        String deserializeMessage = new String(message.getBody());
        MessageDTO messageDTO = JSONObject.parseObject(deserializeMessage, MessageDTO.class);

        messageDTO.getTargetUserIds().forEach(data -> redisUtil.lPush("offline-message-userId" + ":" + data, messageDTO.getMessage()));

        log.info("消息缓存成功，消息Id：{}", messageDTO.getMessageId());
    }
}
