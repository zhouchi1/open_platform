package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款 优惠退款信息-goods_detail
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundPromotionDetailGoodsDTO implements Serializable {

    private static final Long serialVersionUID = 2387462387468723648L;

    /**
     * 商户侧商品编码
     */
    private String merchant_goods_id;

    /**
     * 微信侧商品编码
     */
    private String wechatpay_goods_id;

    /**
     * 商品名称
     */
    private String goods_name;

    /**
     * 商品单价
     */
    private long unit_price;

    /**
     * 商品退款金额
     */
    private long refund_amount;

    /**
     * 商品退货数量
     */
    private int refund_quantity;
}
