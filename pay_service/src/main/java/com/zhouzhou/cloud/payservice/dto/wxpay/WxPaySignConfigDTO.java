package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-04
 * @Description: 微信支付-结算信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPaySignConfigDTO implements Serializable {

    private static final long serialVersionUID = 6748723647823647823L;

    private String merchant_private_key;

    private String sp_mch_id;

    private String api_v3_key;

    private String serial_no;
}
