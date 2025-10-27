package com.zhouzhou.cloud.payservice.request.pay.wxpay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款执行
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundExecuteReq {

    @Schema(name = "商城退款单号")
    private String reFundNumber;

    @Schema(name = "退款金额")
    private BigDecimal reFoundQuantity;

    @Schema(name = "付款金额")
    private BigDecimal reFoundTotal;
}
