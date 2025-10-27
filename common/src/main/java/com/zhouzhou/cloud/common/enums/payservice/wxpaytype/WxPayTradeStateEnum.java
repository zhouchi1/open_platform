package com.zhouzhou.cloud.common.enums.payservice.wxpaytype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付交易状态枚举
 */
@Getter
@AllArgsConstructor
public enum WxPayTradeStateEnum {

    SUCCESS("SUCCESS","支付成功"),

    REFUND("REFUND","转入退款"),

    NOTPAY("NOTPAY","未支付"),

    CLOSED("CLOSED","已关闭"),

    REVOKED("REVOKED","已撤销（刷卡支付）"),

    USERPAYING("USERPAYING","用户支付中"),

    PAYERROR("PAYERROR","支付失败(其他原因，如银行返回失败)"),

    CANCEL("CANCEL","订单超时取消")
    ;

    /**
     * key
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;

    public static WxPayTradeStateEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (WxPayTradeStateEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
