package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款订单-resource解密反序列化 -SceneInfo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayCallBackDecryptSceneInfoDTO implements Serializable {

    private String device_id;
}
