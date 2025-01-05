package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款订单状态查询返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundStatusDTO implements Serializable {


    private static final long serialVersionUID = 6872364872368476232L;

    /**
     * 微信支付退款号
     */
    private String refund_id;

    /**
     * 每日橙退款单号
     */
    private String out_refund_no;

    /**
     * 微信订单号
     */
    private String transaction_id;

    /**
     * 每日橙订单号
     */
    private String out_trade_no;

    /**
     * 退款渠道
     */
    private String channel;

    /**
     * 退款入账账户
     */
    private String user_received_account;

    /**
     * 退款成功时间
     */
    private String success_time;

    /**
     * 退款创建时间
     */
    private String create_time;

    /**
     * 退款状态
     */
    private String status;

    /**
     * 资金账户
     */
    private String funds_account;

    /**
     * 金额信息
     */
    private WxPayReFoundAmountStatusDTO amount;

    /**
     * 优惠退款信息
     */
    private List<WxPayReFoundPromotionDetailDTO> promotion_detail;
}
