package com.zhouzhou.cloud.common.service.interfaces;

import com.zhouzhou.cloud.common.dto.UserIdentityInfoDTO;
import com.zhouzhou.cloud.common.dto.UserNameAndPasswordDTO;
import com.zhouzhou.cloud.common.service.dto.UserLoginDTO;

public interface AuthServiceApi {

    String getTokenFromAuthServer(UserNameAndPasswordDTO userNameAndPasswordDTO);

    Boolean checkTokenValidity(String token);

    UserIdentityInfoDTO queryUserIdentityByToken(String token);
}
