package com.zhouzhou.cloud.authservice.util;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Maps;
import com.zhouzhou.cloud.common.service.dto.UserLoginDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

public class JwtUtils {

    public static final String USER = "USER";

    public static final String TOKEN_TYPE = "TOKEN_TYPE";

    public static final String ACCESS_TOKEN = "token";

    public static final String REFRESH_TOKEN = "refresh_token";

    public static String createAccessToken(String privateKey, UserLoginDTO userAuthDTO, Date expir) {
        byte[] keyBytes = Decoders.BASE64.decode(privateKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        HashMap<String, Object> claims = Maps.newHashMap();
        claims.put(USER, JSONObject.toJSONString(userAuthDTO));
        claims.put(TOKEN_TYPE, ACCESS_TOKEN);

        return Jwts.builder().claims(claims).issuedAt(new Date()).expiration(expir).signWith(key).compact();
    }


    public static String createRefreshToken(String privateKey, Date expir) {

        byte[] keyBytes = Decoders.BASE64.decode(privateKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        HashMap<String, Object> claims = Maps.newHashMap();
        claims.put(TOKEN_TYPE, REFRESH_TOKEN);

        return Jwts.builder().claims(claims).issuedAt(new Date()).expiration(expir).signWith(key).compact();
    }
}
