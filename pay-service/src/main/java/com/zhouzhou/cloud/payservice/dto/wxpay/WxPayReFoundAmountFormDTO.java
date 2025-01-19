package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款订单状态查询返回值-AMOUNT-FORM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundAmountFormDTO implements Serializable {


    private static final long serialVersionUID = 6423786487236487236L;

    /**
     * 出资账户类型
     */
    private String account;

    /**
     * 出资金额
     */
    private Integer amount;
}
