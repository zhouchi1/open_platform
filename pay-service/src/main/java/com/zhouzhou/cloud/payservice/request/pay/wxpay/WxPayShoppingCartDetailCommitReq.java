package com.zhouzhou.cloud.payservice.request.pay.wxpay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 小程序购物车支付下单详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayShoppingCartDetailCommitReq {

    @Schema(name = "商品SKU编码")
    private String skuCode;

    @Schema(name = "商品数量")
    private Integer skuNum;

    @Schema(name = "商品价格")
    private BigDecimal skuPrice;
}
