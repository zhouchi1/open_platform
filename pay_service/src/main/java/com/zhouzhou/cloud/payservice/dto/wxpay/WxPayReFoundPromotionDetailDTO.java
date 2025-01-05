package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款 优惠退款信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundPromotionDetailDTO implements Serializable {

    private static final long serialVersionUID = 6487236478236487623L;

    /**
     * 优惠券Id
     */
    private String promotion_id;

    /**
     * 优惠范围
     */
    private String scope;

    /**
     * 优惠类型
     */
    private String type;

    /**
     * 优惠券面额
     */
    private Integer amount;

    /**
     * 优惠退款金额
     */
    private Integer refund_amount;

    /**
     * 商品列表
     */
    private List<WxPayReFoundPromotionDetailGoodsDTO> goods_detail;
}
