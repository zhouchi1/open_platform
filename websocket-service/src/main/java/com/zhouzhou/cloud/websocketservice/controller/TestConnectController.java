package com.zhouzhou.cloud.websocketservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class TestConnectController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    public String login() {
        // 验证用户密码逻辑...
        String token = JWT.create()
                .withSubject("user123") // 用户唯一标识
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600_000)) // 过期时间
                .sign(Algorithm.HMAC256("your-secret-key"));
        // 存储Token到Redis（可选）
        stringRedisTemplate.opsForValue().set(token, "valid", 1, TimeUnit.HOURS);

        log.info("登录成功 Token:" + token);
        return token;
    }
}
