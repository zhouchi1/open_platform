package com.zhouzhou.cloud.common.service.interfaces;

import com.zhouzhou.cloud.common.dto.UserIdentityInfoDTO;
import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;

public interface AuthServiceApi {

    String getTokenFromAuthServer(UserIdentityConfirmDTO userIdentityConfirmDTO);

    Boolean checkTokenValidity(String token);

    UserIdentityInfoDTO queryUserIdentityByToken(String token);
}
