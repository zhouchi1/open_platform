package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付预支付详情提交订单信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayPromotionDTO implements Serializable {

    private static final long serialVersionUID = 8234234234235423523L;

//    @Schema(name = "订单原价")
//    private Integer cost_price;
//
//    @Schema(name = "商家小票Id")
//    private String invoice_id;

    @Schema(name = "订单商品列表信息-ListMaxSize->6000")
    private List<WxPayGoodsDetailDTO> goods_detail;
}
