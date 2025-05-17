package com.zhouzhou.cloud.authservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouzhou.cloud.authservice.service.AuthConfirmService;
import com.zhouzhou.cloud.common.dto.UserNameAndPasswordDTO;
import com.zhouzhou.cloud.common.entity.TenantAuth;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.common.mapper.TenantAuthMapper;
import com.zhouzhou.cloud.common.mapper.UserInfoMapper;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


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
    public Boolean authConfirm(UserNameAndPasswordDTO userNameAndPasswordDTO) {

        // 查询内部唯一标识
        LambdaQueryWrapper<TenantAuth> tenantQueryWrapper = new LambdaQueryWrapper<>();
        tenantQueryWrapper.eq(TenantAuth::getAppId, userNameAndPasswordDTO.getAppId());
        TenantAuth tenantAuth = tenantMapper.selectOne(tenantQueryWrapper);

        if (ObjectUtils.isEmpty(tenantAuth)){
            return false;
        }

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, userNameAndPasswordDTO.getUserName());
        queryWrapper.eq(UserInfo::getSaasPlatformType, tenantAuth.getSaasPlatformType());
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(userInfo)){
            return false;
        }
        return true;
    }

    @Override
    public SystemUserResp queryUserInfo(UserNameAndPasswordDTO userNameAndPasswordDTO) {

        // 查询内部唯一标识
        LambdaQueryWrapper<TenantAuth> tenantQueryWrapper = new LambdaQueryWrapper<>();
        tenantQueryWrapper.eq(TenantAuth::getAppId, userNameAndPasswordDTO.getAppId());
        TenantAuth tenantAuth = tenantMapper.selectOne(tenantQueryWrapper);

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, userNameAndPasswordDTO.getUserName());
        queryWrapper.eq(UserInfo::getSaasPlatformType, tenantAuth.getSaasPlatformType());
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);

        SystemUserResp systemUserResp = new SystemUserResp();
        systemUserResp.setUserId(String.valueOf(userInfo.getId()));
        systemUserResp.setSaasPlatformType(tenantAuth.getSaasPlatformType());
        return systemUserResp;
    }
}
