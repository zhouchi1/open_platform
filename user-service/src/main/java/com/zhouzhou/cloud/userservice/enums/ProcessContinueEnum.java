package com.zhouzhou.cloud.userservice.enums;

import com.zhouzhou.cloud.common.service.excepetions.BizException;

import java.util.Arrays;

public enum ProcessContinueEnum {

    SKIP(1), NO_SKIP(2);

    private final int code;

    ProcessContinueEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ProcessContinueEnum fromCode(int code) {
        return Arrays.stream(values())
                .filter(e -> e.code == code)
                .findFirst()
                .orElseThrow(() -> new BizException("不支持的跳过类型: " + code));
    }

}
