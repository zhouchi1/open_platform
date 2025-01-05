package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款 解密信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundCallBackDecryptDTO implements Serializable {

    private static final long serialVersionUID = 6784321648726347862L;

    private String mchid;

    private String transaction_id;

    private String out_trade_no;

    private String refund_id;

    private String out_refund_no;

    private String refund_status;

    private String success_time;

    private String user_received_account;

    private WxPayReFoundCallBackDecryptAmountDTO amount;
}
