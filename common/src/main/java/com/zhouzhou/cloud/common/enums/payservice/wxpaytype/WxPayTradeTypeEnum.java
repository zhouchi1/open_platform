package com.zhouzhou.cloud.common.enums.payservice.wxpaytype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付交易类型枚举
 */
@Getter
@AllArgsConstructor
public enum WxPayTradeTypeEnum {


    JSAPI("JSAPI", "公众号支付"),

    NATIVE("NATIVE", "Native支付"),

    APP("APP", "APP支付"),

    MICROPAY("MICROPAY", "付款码支付"),

    MWEB("MWEB", "H5支付"),

    FACEPAY("FACEPAY", "刷脸支付");

    /**
     * key
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;

    public static WxPayTradeTypeEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (WxPayTradeTypeEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
