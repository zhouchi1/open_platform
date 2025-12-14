package com.zhouzhou.cloud.userservice.dubboservice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.common.service.interfaces.UserRpcServer;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;
import com.zhouzhou.cloud.common.mapper.UserInfoMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

@Slf4j
@DubboService(version = "1.0.0")
public class UserRpcServerImplV1 implements UserRpcServer {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Boolean authConfirm(UserIdentityConfirmDTO userIdentityConfirmDTO) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getId, userIdentityConfirmDTO.getUserId());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

        if (ObjectUtils.isEmpty(userInfo)){
            return Boolean.FALSE;
        }

        // 输入的密码错误
        if (!StringUtils.equals(DigestUtils.md5DigestAsHex(userIdentityConfirmDTO.getUserPassword().getBytes()),
                userInfo.getUserPassword())){
            return Boolean.FALSE;
        }

        return !ObjectUtils.isEmpty(userInfo);
    }

    @Override
    public SystemUserResp queryUserInfo(UserIdentityConfirmDTO userIdentityConfirmDTO) {

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getId, userIdentityConfirmDTO.getUserId());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

        SystemUserResp systemUserResp = new SystemUserResp();
        systemUserResp.setUserId(String.valueOf(userInfo.getId()));
        return systemUserResp;
    }

    @Override
    public UserInfo getUserById(String userId) {
        return null;
    }
}
