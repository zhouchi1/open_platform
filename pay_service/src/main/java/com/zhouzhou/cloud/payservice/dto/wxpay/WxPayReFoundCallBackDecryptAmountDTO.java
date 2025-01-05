package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款 解密信息-Amount
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundCallBackDecryptAmountDTO implements Serializable {

    private Integer total;

    private Integer refund;

    private Integer payer_total;

    private Integer payer_refund;
}
