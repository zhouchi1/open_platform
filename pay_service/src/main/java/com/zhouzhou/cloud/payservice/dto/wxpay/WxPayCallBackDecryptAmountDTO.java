package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付订单-resource解密反序列化 -Amount
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayCallBackDecryptAmountDTO implements Serializable {

    private static final long serialVersionUID = 2348762378462378642L;

    private Integer payer_total;

    private Integer total;

    private String currency;

    private String payer_currency;
}
