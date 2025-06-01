package com.zhouzhou.cloud.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.userservice.dto.ProcessSaasUserInfoDTO;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-29
 * @Description: 用户信息服务层接口
 */
public interface UserInfoService extends IService<UserInfo> {

    void processSaasUser(ProcessSaasUserInfoDTO processSaasUserInfoDTO,String appId);
}
