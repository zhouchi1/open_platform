package com.zhouzhou.cloud.websocketservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import com.zhouzhou.cloud.websocketservice.service.SendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class TestConnectController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SendMessageService sendMessageService;

    @PostMapping("/login")
    public String login(String userId) {
        // 验证用户密码逻辑...
        String token = JWT.create()
                .withSubject(userId) // 用户唯一标识
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600_000)) // 过期时间
                .sign(Algorithm.HMAC256("your-secret-key"));
        // 存储Token到Redis（可选）
        stringRedisTemplate.opsForValue().set(token, "valid", 1, TimeUnit.HOURS);

        log.info("登录成功 Token:" + token);
        return token;
    }

    @PostMapping("/testSendBroadcastMessage")
    public void testSendBroadcastMessage(@RequestBody MessageTransportDTO messageTransportDTO){
        sendMessageService.sendBroadcastMessage(messageTransportDTO);
    }

    @PostMapping("/testSendPrivateMessage")
    public void testSendPrivateMessage(@RequestBody MessageTransportDTO messageTransportDTO){
        sendMessageService.sendPrivateMessage(messageTransportDTO);
    }
}
