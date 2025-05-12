package com.zhouzhou.cloud.authservice.service;

import com.zhouzhou.cloud.common.dto.UserNameAndPasswordDTO;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-10
 * @Description: 身份认证服务类
 */
public interface AuthConfirmService {

    Boolean authConfirm(UserNameAndPasswordDTO userNameAndPasswordDTO);

    SystemUserResp queryUserInfo(UserNameAndPasswordDTO userNameAndPasswordDTO);
}
