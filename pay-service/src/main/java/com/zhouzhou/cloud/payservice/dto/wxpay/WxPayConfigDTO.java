package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付预支付提交订单信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayConfigDTO implements Serializable {

    private static final long serialVersionUID = 3264786237846238764L;

    @ApiModelProperty("小程序appId")
    private String sp_app_id;

    @ApiModelProperty("商户号")
    private String sp_mch_id;

    @ApiModelProperty("子商户号")
    private String sub_mch_id;

    @ApiModelProperty("子商户appId")
    private String sub_app_id;

    @ApiModelProperty("小程序密钥")
    private String applet_secret;

    @ApiModelProperty("微信支付URL")
    private String pay_url;

    @ApiModelProperty("微信支付查询URL")
    private String order_query_url;

    @ApiModelProperty("退款接口URL")
    private String refund_url;

    @ApiModelProperty("退款查询接口URL")
    private String refund_query_url;

    @ApiModelProperty("撤销订单URL")
    private String cancel_order_url;

    @ApiModelProperty("V3密钥")
    private String api_v3_key;

    @ApiModelProperty("商户私钥")
    private String merchant_private_key;

    @ApiModelProperty("证书序列号")
    private String serial_no;

    @ApiModelProperty("支付结果通知回调URL")
    private String notify_url;

    @ApiModelProperty("退款结果通知回调URL")
    private String refund_notify_url;
}
