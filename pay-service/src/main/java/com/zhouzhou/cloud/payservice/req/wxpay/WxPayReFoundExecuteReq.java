package com.zhouzhou.cloud.payservice.req.wxpay;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("每日橙退款单号")
    private String reFundNumber;

    @ApiModelProperty("退款金额")
    private BigDecimal reFoundQuantity;

    @ApiModelProperty("付款金额")
    private BigDecimal reFoundTotal;
}
