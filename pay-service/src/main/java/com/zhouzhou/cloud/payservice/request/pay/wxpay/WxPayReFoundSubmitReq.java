package com.zhouzhou.cloud.payservice.request.pay.wxpay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款申请提交
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundSubmitReq {

    @Schema(name = "每日橙订单号")
    private String outTradeNo;

    @Schema(name = "退款金额")
    private BigDecimal reFoundQuantity;

    @Schema(name = "退款原因")
    private String reason;
}
