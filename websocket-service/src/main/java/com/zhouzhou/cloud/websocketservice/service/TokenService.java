package com.zhouzhou.cloud.websocketservice.service;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.common.service.dto.UserLoginDTO;
import com.zhouzhou.cloud.common.service.enums.RedisEnum;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;
import com.zhouzhou.cloud.websocketservice.dto.SecurityCheckCompleteDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: Token身份认证服务
 */
@Slf4j
@Component
public class TokenService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public SecurityCheckCompleteDTO validateTokenAndReturnUserIdCode(String token, String deviceType) {
        try {
            if (StringUtils.isBlank(deviceType)) {
                log.info("消息发送设备类型识别失败！");
                return null;
            } else if (deviceType.contains(SHOPPING_APPLET)) {
                return validateUserToken(token);
            }
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private SecurityCheckCompleteDTO validateUserToken(String token) {
        String userInfo = stringRedisTemplate.opsForValue().get(RedisEnum.ACCESS_TOKEN.createKey(token));
        return getUserInfo(userInfo, UserLoginDTO.class);
    }

    private <T> SecurityCheckCompleteDTO getUserInfo(String json, Class<T> clazz) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        T dto = JSON.parseObject(json, clazz);
        if (ObjectUtils.isEmpty(dto)) {
            return null;
        }

        if (dto instanceof UserLoginDTO) {
            SystemUserResp userResp = ((UserLoginDTO) dto).getUserResp();
            SecurityCheckCompleteDTO securityCheckCompleteDTO = new SecurityCheckCompleteDTO();
            securityCheckCompleteDTO.setUserId(String.valueOf(userResp.getUserId()));
            securityCheckCompleteDTO.setUserCode(String.valueOf(userResp.getUserCode()));
            return securityCheckCompleteDTO;
        }
        return null;
    }
}
