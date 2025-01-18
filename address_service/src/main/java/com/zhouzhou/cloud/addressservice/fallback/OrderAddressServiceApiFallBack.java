package com.zhouzhou.cloud.addressservice.fallback;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OrderAddressServiceApiFallBack {

    /**
     * 当方法中出现异常时 会直接调用这个方法
     * @param throwable 抛出的异常
     * @return 默认异常响应
     */
    public static String getAddressInfoFallback(Throwable throwable) {
        return "默认地址: 北京市朝阳区";
    }
}
