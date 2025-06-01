package com.zhouzhou.cloud.userservice.dubboservice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;
import com.zhouzhou.cloud.common.entity.TenantAuth;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.common.service.interfaces.UserServiceApi;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;
import com.zhouzhou.cloud.common.utils.SignUtil;
import com.zhouzhou.cloud.userservice.mapper.TenantAuthMapper;
import com.zhouzhou.cloud.userservice.mapper.UserInfoMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@DubboService(version = "1.0.0")
public class UserServiceImplV1 implements UserServiceApi {

    @Resource
    private TenantAuthMapper tenantAuthMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Boolean authConfirm(UserIdentityConfirmDTO userIdentityConfirmDTO) {
        // 时间戳过期时间判定
        long currentTime = Instant.now().toEpochMilli();
        if (Math.abs(currentTime - userIdentityConfirmDTO.getTimestamp()) > 1000000) {
            return false;
        }

        // 查询内部唯一标识
        LambdaQueryWrapper<TenantAuth> tenantQueryWrapper = new LambdaQueryWrapper<>();
        tenantQueryWrapper.eq(TenantAuth::getAppId, userIdentityConfirmDTO.getAppId());
        TenantAuth tenantAuth = tenantAuthMapper.selectOne(tenantQueryWrapper);

        // 不存在的第三方 拒绝授权
        if (ObjectUtils.isEmpty(tenantAuth)) {
            return false;
        }

        // 不存在的第三方用户 拒绝授权
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, userIdentityConfirmDTO.getUserName());
        queryWrapper.eq(UserInfo::getAppId, tenantAuth.getAppId());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(userInfo)) {
            return false;
        }

        // 根据加密算法匹配匹配是否为合法请求
        Map<String, String> params = new HashMap<>();
        params.put("appId", userIdentityConfirmDTO.getAppId());
        params.put("nonce", userIdentityConfirmDTO.getNonce());
        params.put("timestamp", String.valueOf(userIdentityConfirmDTO.getTimestamp()));
        params.put("userName", userIdentityConfirmDTO.getUserName());

        String signature = SignUtil.generateSignature(params, tenantAuth.getAppPublicSecret());

        // 签名不匹配 疑似收到签名替换攻击 拒绝授权
        return signature.equals(userIdentityConfirmDTO.getSign());
    }

    @Override
    public SystemUserResp queryUserInfo(UserIdentityConfirmDTO userIdentityConfirmDTO) {
        // 查询内部唯一标识
        LambdaQueryWrapper<TenantAuth> tenantQueryWrapper = new LambdaQueryWrapper<>();
        tenantQueryWrapper.eq(TenantAuth::getAppId, userIdentityConfirmDTO.getAppId());
        TenantAuth tenantAuth = tenantAuthMapper.selectOne(tenantQueryWrapper);

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, userIdentityConfirmDTO.getUserName());
        queryWrapper.eq(UserInfo::getAppId, tenantAuth.getAppId());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

        SystemUserResp systemUserResp = new SystemUserResp();
        systemUserResp.setUserId(String.valueOf(userInfo.getId()));
        systemUserResp.setSaasPlatformType(tenantAuth.getAppId());
        return systemUserResp;
    }
}
