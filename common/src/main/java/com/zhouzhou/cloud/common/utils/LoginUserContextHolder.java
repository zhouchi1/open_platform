package com.zhouzhou.cloud.common.utils;

import com.zhouzhou.cloud.common.service.dto.UserLoginDTO;

public class LoginUserContextHolder {

    private static final ThreadLocal<UserLoginDTO> DATASOURCE_CONTEXT_KEY_HOLDER = new ThreadLocal<>();

    public static void set(UserLoginDTO loginUserDTO){
        DATASOURCE_CONTEXT_KEY_HOLDER.set(loginUserDTO);
    }

    public static UserLoginDTO get(){
        return DATASOURCE_CONTEXT_KEY_HOLDER.get();
    }

    public static void remove(){
        DATASOURCE_CONTEXT_KEY_HOLDER.remove();
    }
}
