package com.zhouzhou.cloud.common.service.interfaces;

import com.zhouzhou.cloud.common.dto.UserIdentityInfoDTO;
import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;

public interface AuthRpcServer {

    String checkUserNameAndPassword(UserIdentityConfirmDTO userIdentityConfirmDTO) throws Exception;

    Boolean checkTokenValidity(String token);

    UserIdentityInfoDTO queryUserIdentityByToken(String token);
}
