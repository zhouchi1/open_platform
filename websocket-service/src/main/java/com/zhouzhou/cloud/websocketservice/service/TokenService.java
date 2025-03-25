package com.zhouzhou.cloud.websocketservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TokenService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final Algorithm algorithm = Algorithm.HMAC256("your-secret-key");

    private final JWTVerifier verifier = JWT.require(algorithm).build();

    public boolean validateToken(String token) {
        try {
            // 验证JWT签名和过期时间
            DecodedJWT decodedJWT = verifier.verify(token);
            // 检查Redis中Token是否存在（可选，用于强制失效场景）
            return stringRedisTemplate.hasKey(token);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject(); // 或根据Claim名称获取：decodedJWT.getClaim("userId").asString()
    }
}
