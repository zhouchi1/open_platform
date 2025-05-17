package com.zhouzhou.cloud.authservice.service;

import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-10
 * @Description: 身份认证服务类
 */
public interface AuthConfirmService {

    Boolean authConfirm(UserIdentityConfirmDTO userIdentityConfirmDTO);

    SystemUserResp queryUserInfo(UserIdentityConfirmDTO userIdentityConfirmDTO);
}
