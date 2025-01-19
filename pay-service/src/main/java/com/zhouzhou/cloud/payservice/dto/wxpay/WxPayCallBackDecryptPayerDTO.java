package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付订单-resource解密反序列化 -payer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayCallBackDecryptPayerDTO implements Serializable {

    private static final long serialVersionUID = 2348762378462908642L;

    private String openid;
}
