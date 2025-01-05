package com.zhouzhou.cloud.payservice.req.wxpay;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("商品SKU编码")
    private String skuCode;

    @ApiModelProperty("商品数量")
    private Integer skuNum;

    @ApiModelProperty("商品价格")
    private BigDecimal skuPrice;
}
