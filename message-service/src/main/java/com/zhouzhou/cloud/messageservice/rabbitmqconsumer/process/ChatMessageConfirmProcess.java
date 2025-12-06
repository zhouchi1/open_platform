package com.zhouzhou.cloud.messageservice.rabbitmqconsumer.process;

import com.alibaba.fastjson2.JSONObject;
import com.zhouzhou.cloud.common.dto.MessageDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.zhouzhou.cloud.messageservice.constant.Constants.*;


@Slf4j
@Component
public class ChatMessageConfirmProcess {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void processMessage(Message message) {
        String deserializeMessage = new String(message.getBody());
        MessageDTO messageDTO = JSONObject.parseObject(deserializeMessage, MessageDTO.class);

        log.info("消息状态更新成功，消息Id：{}", messageDTO.getMessageId());
    }

    private void deleteOffLineMessage(String messageId, String userId) {
        stringRedisTemplate.opsForHash().delete(OFFLINE_MESSAGE_BY_USER + userId, messageId);
        stringRedisTemplate.opsForZSet().remove(OFFLINE_MESSAGE_BY_USER_EXPIRE, messageId);
    }
}
