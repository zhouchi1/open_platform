package com.zhouzhou.cloud.websocketservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-04-05
 * @Description: 设备类型标识枚举
 */
@Getter
@AllArgsConstructor
public enum DeviceTypeEnum {

    SHOPPING_PC("SHOPPING_PC", "商城平台端"),

    SHOPPING_APPLET("SHOPPING_APPLET", "商城小程序端"),

    DRIVER_APPLET("DRIVER_APPLET", "司机小程序端"),

    CASH_REGISTER("CASH_REGISTER", "收银机端")
    ;

    private final String code;

    private final String desc;

    public static DeviceTypeEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (DeviceTypeEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
