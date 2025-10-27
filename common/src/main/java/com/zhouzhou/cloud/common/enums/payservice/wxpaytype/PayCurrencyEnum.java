package com.zhouzhou.cloud.common.enums.payservice.wxpaytype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付币种枚举
 */
@Getter
@AllArgsConstructor
public enum PayCurrencyEnum {

    CNY("CNY", "人民币"),
    ;

    /**
     * key
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;

    public static PayCurrencyEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (PayCurrencyEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
