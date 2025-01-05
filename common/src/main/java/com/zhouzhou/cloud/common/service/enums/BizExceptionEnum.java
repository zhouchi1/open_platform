package com.zhouzhou.cloud.common.service.enums;

import com.zhouzhou.cloud.common.service.excepetions.IErrorCode;

import java.io.Serializable;

/**
 * @author sunqingrui
 * @description 用户登录相关枚举
 */

public enum BizExceptionEnum implements IErrorCode {

    /**
     * 无权访问状态码
     */
    OFF_JOB_PERMISSION_DENIED("10024", "账户状态禁用,无权访问"),
    /**
     * 超时状态码
     */
    TIME_OUT("0048", ""),
    ;

    private final String code;
    private final String desc;

    private BizExceptionEnum(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BizExceptionEnum getBizExceptionEnumByCode(short code) {
        for (BizExceptionEnum innerEnum : BizExceptionEnum.values()) {
            if (innerEnum.getCode().equals(code)) {
                return innerEnum;
            }
        }
        return null;
    }

    @Override
    public Serializable getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
