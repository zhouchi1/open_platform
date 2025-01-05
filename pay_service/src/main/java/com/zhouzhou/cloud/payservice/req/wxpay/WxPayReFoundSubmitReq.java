package com.zhouzhou.cloud.payservice.req.wxpay;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("每日橙订单号")
    private String outTradeNo;

    @ApiModelProperty("退款金额")
    private BigDecimal reFoundQuantity;

    @ApiModelProperty("退款原因")
    private String reason;
}
