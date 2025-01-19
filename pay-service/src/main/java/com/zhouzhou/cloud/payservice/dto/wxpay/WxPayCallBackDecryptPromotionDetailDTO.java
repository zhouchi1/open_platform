package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付订单-resource解密反序列化 -Promotion
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayCallBackDecryptPromotionDetailDTO implements Serializable {

    private static final long serialVersionUID = 2348762378461908642L;

    private Integer amount;

    private Integer wechatpay_contribute;

    private String coupon_id;

    private String scope;

    private Integer merchant_contribute;

    private String name;

    private Integer other_contribute;

    private String currency;

    private String stock_id;

    private List<WxPayCallBackDecryptPromotionGoodsDTO> goods_detail;
}
