package com.zhouzhou.cloud.payservice.request.pay.wxpay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 小程序购物车支付下单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayShoppingCartCommitReq{


    @Schema(name = "订单支付金额")
    private BigDecimal totalPrice;

    @Schema(name = "购物车商品信息提交")
    List<WxPayShoppingCartDetailCommitReq> wxPayShoppingCartDetailCommitReqList;
}
