package com.zhouzhou.cloud.authservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouzhou.cloud.authservice.service.AuthConfirmService;
import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;
import com.zhouzhou.cloud.common.entity.TenantAuth;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.common.mapper.TenantAuthMapper;
import com.zhouzhou.cloud.common.mapper.UserInfoMapper;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;
import com.zhouzhou.cloud.common.utils.SignUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-10
 * @Description: 身份认证服务实现类
 */
@Slf4j
@Service
public class AuthConfirmServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements AuthConfirmService {

    @Resource
    private TenantAuthMapper tenantMapper;

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
        TenantAuth tenantAuth = tenantMapper.selectOne(tenantQueryWrapper);

        // 不存在的第三方 拒绝授权
        if (ObjectUtils.isEmpty(tenantAuth)) {
            return false;
        }

        // 不存在的第三方用户 拒绝授权
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, userIdentityConfirmDTO.getUserName());
        queryWrapper.eq(UserInfo::getSaasPlatformType, tenantAuth.getSaasPlatformType());
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(userInfo)) {
            return false;
        }

        // 根据加密算法匹配匹配是否为合法请求
        Map<String, String> params = new HashMap<>();
        params.put("appId", userIdentityConfirmDTO.getAppId());
        params.put("nonce", userIdentityConfirmDTO.getNonce());
        params.put("timestamp", String.valueOf(userIdentityConfirmDTO.getTimestamp()));
        params.put("userName", userIdentityConfirmDTO.getUserName());

        String signature = SignUtil.generateSignature(params, tenantAuth.getAppSecret());

        // 签名不匹配 疑似收到签名替换攻击 拒绝授权
        return signature.equals(userIdentityConfirmDTO.getSign());
    }

    @Override
    public SystemUserResp queryUserInfo(UserIdentityConfirmDTO userIdentityConfirmDTO) {

        // 查询内部唯一标识
        LambdaQueryWrapper<TenantAuth> tenantQueryWrapper = new LambdaQueryWrapper<>();
        tenantQueryWrapper.eq(TenantAuth::getAppId, userIdentityConfirmDTO.getAppId());
        TenantAuth tenantAuth = tenantMapper.selectOne(tenantQueryWrapper);

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, userIdentityConfirmDTO.getUserName());
        queryWrapper.eq(UserInfo::getSaasPlatformType, tenantAuth.getSaasPlatformType());
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);

        SystemUserResp systemUserResp = new SystemUserResp();
        systemUserResp.setUserId(String.valueOf(userInfo.getId()));
        systemUserResp.setSaasPlatformType(tenantAuth.getSaasPlatformType());
        return systemUserResp;
    }
}
