package com.zhouzhou.cloud.websocketservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: Token身份认证服务
 */
@Component
public class TokenService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final Algorithm algorithm = Algorithm.HMAC256("your-secret-key");

    private final JWTVerifier verifier = JWT.require(algorithm).build();

    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return stringRedisTemplate.hasKey(token);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }
}
