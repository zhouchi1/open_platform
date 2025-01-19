package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款订单状态查询返回值-AMOUNT
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundAmountStatusDTO implements Serializable {

    private static final long serialVersionUID = 8237487236487236478L;

    /**
     * 订单金额
     */
    private Integer total;

    /**
     * 退款金额
     */
    private Integer refund;

    /**
     * 用户支付金额
     */
    private Integer payer_total;

    /**
     * 用户退款金额
     */
    private Integer payer_refund;

    /**
     * 应结退款金额
     */
    private Integer settlement_refund;

    /**
     * 应结订单金额
     */
    private Integer settlement_total;

    /**
     * 优惠退款金额
     */
    private Integer discount_refund;

    /**
     * 退款币种
     */
    private String currency;

    /**
     * 手续费退款金额
     */
    private Integer refund_fee;

    /**
     * 退款出资账户及金额
     */
    private List<WxPayReFoundAmountFormDTO> from;
}
