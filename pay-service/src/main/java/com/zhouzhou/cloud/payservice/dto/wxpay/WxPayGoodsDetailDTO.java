package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-04
 * @Description: 微信支付订单详情信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayGoodsDetailDTO implements Serializable {

    private static final Long serialVersionUID = 6542736487236487236L;

    @ApiModelProperty("每日橙方面的商品编码-必填")
    private String merchant_goods_id;

//    @ApiModelProperty("微信方面定义的通用的商品编码-非必填")
//    private String wechatpay_goods_id;
//
//    @ApiModelProperty("每日橙商品名称-非必填")
//    private String goods_name;

    @ApiModelProperty("每日橙商品单价-单位为分-必填")
    private Integer unit_price;

    @ApiModelProperty("每日橙商品数量-必填")
    private Integer quantity;
}
