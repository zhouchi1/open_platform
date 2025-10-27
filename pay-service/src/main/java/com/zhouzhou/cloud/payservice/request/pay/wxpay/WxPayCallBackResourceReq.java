package com.zhouzhou.cloud.payservice.request.pay.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款回调请求-resource
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayCallBackResourceReq {


    /**
     * 原始回调类型
     */
    private String original_type;

    /**
     * 加密的加密算法
     */
    private String algorithm;

    /**
     * Base64编码后的开启/停用结果数据密文
     */
    private String ciphertext;

    /**
     * 附加数据
     */
    private String associated_data;

    /**
     * 随机串
     */
    private String nonce;
}
