package com.zhouzhou.cloud.userservice.enums;

import com.zhouzhou.cloud.common.service.excepetions.BizException;

import java.util.Arrays;

public enum ProcessTypeEnum {

    ADD(1), DELETE(2), ENABLE(3), DISABLE(4);

    private final int code;

    ProcessTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ProcessTypeEnum fromCode(int code) {
        return Arrays.stream(values())
                .filter(e -> e.code == code)
                .findFirst()
                .orElseThrow(() -> new BizException("不支持的处理类型: " + code));
    }
}
