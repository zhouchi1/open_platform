package com.zhouzhou.cloud.messageservice.rabbitmqconsumer.process;

import com.alibaba.fastjson2.JSONObject;
import com.zhouzhou.cloud.common.dto.MessageDTO;
import com.zhouzhou.cloud.common.entity.MessageCenter;
import com.zhouzhou.cloud.common.entity.MessageCenterDetail;
import com.zhouzhou.cloud.common.mapper.MessageCenterDetailMapper;
import com.zhouzhou.cloud.common.mapper.MessageCenterMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ChatMessagePersistenceProcess {

    @Resource
    private MessageCenterMapper messageCenterMapper;

    @Resource
    private MessageCenterDetailMapper messageCenterDetailMapper;

    public void processMessage(Message message) {

        String deserializeMessage = new String(message.getBody());
        MessageDTO messageDTO = JSONObject.parseObject(deserializeMessage, MessageDTO.class);

        MessageCenter messageCenter = new MessageCenter();
        messageCenter.setMessageId(messageDTO.getMessageId());
        messageCenter.setMessage(messageDTO.getMessage());
        messageCenter.setMessageType(messageDTO.getType());
        messageCenter.setCreateNo(messageDTO.getSendUserId());
        messageCenter.setCreateTime(LocalDateTime.now());
        messageCenter.setUpdateNo(messageDTO.getSendUserId());
        messageCenter.setUpdateTime(LocalDateTime.now());
        messageCenter.setMark(Boolean.TRUE);
        messageCenterMapper.insert(messageCenter);

        messageDTO.getTargetUserIds().forEach(data ->{
            MessageCenterDetail messageCenterDetail = new MessageCenterDetail();
            messageCenterDetail.setMessageMasterId(String.valueOf(messageCenter.getId()));
            messageCenterDetail.setReceiveMessageUserId(data);
            messageCenterDetail.setReceiveStatus(Boolean.FALSE);
            messageCenterDetail.setCreateNo(messageDTO.getSendUserId());
            messageCenterDetail.setCreateTime(LocalDateTime.now());
            messageCenterDetail.setUpdateNo(messageDTO.getSendUserId());
            messageCenterDetail.setUpdateTime(LocalDateTime.now());
            messageCenterDetail.setMark(Boolean.TRUE);
            messageCenterDetailMapper.insert(messageCenterDetail);
        });

        log.info("消息持久化成功，消息Id：{}", messageDTO.getMessageId());
    }
}
