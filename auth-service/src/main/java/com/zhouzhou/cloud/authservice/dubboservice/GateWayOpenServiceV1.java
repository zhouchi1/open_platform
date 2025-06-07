package com.zhouzhou.cloud.authservice.dubboservice;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zhouzhou.cloud.authservice.util.JwtUtils;
import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;
import com.zhouzhou.cloud.common.service.dto.UserLoginDTO;
import com.zhouzhou.cloud.common.service.interfaces.AuthServiceApi;
import com.zhouzhou.cloud.common.service.interfaces.UserServiceApi;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.common.dto.UserIdentityInfoDTO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.ObjectUtils;

import java.util.Date;

import static com.zhouzhou.cloud.common.constant.AuthConstant.UN_AUTH;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-30
 * @Description: 获取登录Token服务实现
 */
@RefreshScope
@DubboService(version = "1.0.0")
public class GateWayOpenServiceV1 implements AuthServiceApi {

    @Resource
    private RedisUtil redisUtil;

    @Value("${jwt.secret}")
    public String secretJwt;

    @Value("${jwt.expire}")
    public int expire;

    @DubboReference(version = "1.0.0")
    private UserServiceApi userServiceApi;

    /**
     * 获取授权Token
     * @param userIdentityConfirmDTO 用户名以及密码
     * @return valid token or UN_AUTH
     */
    @Override
    public String getTokenFromAuthServer(UserIdentityConfirmDTO userIdentityConfirmDTO) throws Exception {

        // 验证saas平台与用户名对应关系是否成立
        Boolean isAuth = userServiceApi.authConfirm(userIdentityConfirmDTO);

        // 如果身份验证不通过则直接返回未授权字符串代表未授权
        if (!isAuth){
            return UN_AUTH;
        }

        // 授权通过组装用户信息用来生成JWT Token
        UserLoginDTO userLoginDTO = new UserLoginDTO();

        // 查询用户基础信息
        SystemUserResp systemUserResp = userServiceApi.queryUserInfo(userIdentityConfirmDTO);
        userLoginDTO.setUserResp(systemUserResp);

        // 获取JWT Token 失效时间为1小时
        String accessToken = JwtUtils.createAccessToken(secretJwt, userLoginDTO, DateUtils.addSeconds(new Date(), expire));

        // 将生成的放置在Redis中缓存 用于服务端主动的进行Token失效处理 并用于之后的身份认证校验（但违背了JWT设计理念-去除服务端中心化存储）
        redisUtil.set(accessToken, JSON.toJSONString(userLoginDTO), expire);

        // 将生成的JWT Token返回至客户端
        return accessToken;
    }


    /**
     * 验证Token是否有效
     * @param token token
     * @return token是否有效
     */
    @Override
    public Boolean checkTokenValidity(String token) {
        return !ObjectUtils.isEmpty(redisUtil.get(token));
    }

    /**
     * 根据token获取用户信息
     * @param token token
     * @return 用户信息
     */
    @Override
    public UserIdentityInfoDTO queryUserIdentityByToken(String token) {
        UserLoginDTO userLoginDTO = JSONObject.parseObject((String) redisUtil.get(token), UserLoginDTO.class);

        UserIdentityInfoDTO userIdentityInfoDTO = new UserIdentityInfoDTO();
        userIdentityInfoDTO.setUserId(userLoginDTO.getUserResp().getUserId());
        userIdentityInfoDTO.setAppId(userLoginDTO.getUserResp().getAppId());
        return userIdentityInfoDTO;
    }


}
