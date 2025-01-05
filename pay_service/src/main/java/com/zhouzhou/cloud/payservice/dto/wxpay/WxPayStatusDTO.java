package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付订单状态查询返回值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayStatusDTO implements Serializable {

    private static final Long serialVersionUID = 6863428764872368476L;

    /**
     * 合作伙伴商户号
     */
    private String sp_appid;

    /**
     * 商户号
     */
    private String sp_mchid;

    /**
     * 子商户appid
     */
    private String sub_appid;

    /**
     * 子商户号
     */
    private String sub_mchid;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 微信订单号
     */
    private String transaction_id;

    /**
     * 交易类型
     */
    private String trade_type;

    /**
     * 交易状态
     */
    private String trade_state;

    /**
     * 交易状态描述
     */
    private String trade_state_desc;

    /**
     * 银行类型
     */
    private String bank_type;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 支付完成时间
     */
    private String success_time;
}
