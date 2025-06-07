package com.zhouzhou.cloud.common.service.interfaces;

import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;

public interface UserServiceApi {

    Boolean authConfirm(UserIdentityConfirmDTO userIdentityConfirmDTO) throws Exception;

    SystemUserResp queryUserInfo(UserIdentityConfirmDTO userIdentityConfirmDTO);
}
