package com.zhouzhou.cloud.authservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouzhou.cloud.authservice.service.AuthConfirmService;
import com.zhouzhou.cloud.common.dto.UserNameAndPasswordDTO;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.common.mapper.UserInfoMapper;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;
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

    @Override
    public Boolean authConfirm(UserNameAndPasswordDTO userNameAndPasswordDTO) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, userNameAndPasswordDTO.getUserName());
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(userInfo)){
            return false;
        }
        if (!userInfo.getPassword().equals(userNameAndPasswordDTO.getPassword())){
            return false;
        }
        return true;
    }

    @Override
    public SystemUserResp queryUserInfo(UserNameAndPasswordDTO userNameAndPasswordDTO) {

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserName, userNameAndPasswordDTO.getUserName());
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);

        SystemUserResp systemUserResp = new SystemUserResp();
        systemUserResp.setUserId(userInfo.getId());
        systemUserResp.setUserName(userInfo.getUserName());
        return systemUserResp;
    }
}
