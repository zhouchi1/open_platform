package com.zhouzhou.cloud.websocketservice.service;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.WEBSOCKET_BROADCAST;
import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.WEBSOCKET_PRIVATE;

@Slf4j
@Service
public class SendMessageService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void sendBroadcastMessage(MessageTransportDTO message) {
        stringRedisTemplate.convertAndSend(WEBSOCKET_BROADCAST, message.toString());
    }

    public void sendPrivateMessage(MessageTransportDTO message) {
        stringRedisTemplate.convertAndSend(WEBSOCKET_PRIVATE, JSON.toJSONString(message));
    }
}
