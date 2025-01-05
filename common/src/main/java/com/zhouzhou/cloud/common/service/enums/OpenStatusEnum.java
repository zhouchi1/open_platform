package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 打开状态
 * @author: ryf
 * @Date: 2024/1/30 9:21
 */
@Getter
@AllArgsConstructor
public enum OpenStatusEnum {

    /**
     * 打开
     */
    ON("ON", "打开"),
    /**
     * 关闭
     */
    OFF("OFF", "关闭"),

    ;

    /**
     * key
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;

    public static OpenStatusEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (OpenStatusEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
