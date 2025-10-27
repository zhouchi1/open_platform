package com.zhouzhou.cloud.common.enums.payservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum BankType {

    China_Merchants_Bank("China_Merchants_Bank", "中国招商银行"),

    China_Construction_Bank("China_Construction_Bank","中国建设银行")

    ;

    /**
     * key
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;

    public static BankType matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (BankType value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
