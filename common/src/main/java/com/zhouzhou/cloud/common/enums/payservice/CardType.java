package com.zhouzhou.cloud.common.enums.payservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CardType {

    CREDIT("CREDIT", "信用卡"),

    ;

    /**
     * key
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;

    public static CardType matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (CardType value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
