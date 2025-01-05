package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付订单-resource解密反序列化 -Promotion-goods
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayCallBackDecryptPromotionGoodsDTO implements Serializable {

    private static final long serialVersionUID = 2348762378462908622L;

    private String goods_remark;

    private int quantity;

    private int discount_amount;

    private String goods_id;

    private int unit_price;
}
