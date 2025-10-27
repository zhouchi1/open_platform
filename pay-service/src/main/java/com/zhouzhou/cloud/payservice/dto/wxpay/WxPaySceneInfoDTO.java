package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-04
 * @Description: 微信支付场景信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPaySceneInfoDTO implements Serializable {

    private static final Long serialVersionUID = 6487236487236487623L;

    @Schema(name = "用户终端Ip-必填")
    private String payer_client_ip;

    @Schema(name = "商户端设备号-非必填")
    private String device_id;

    @Schema(name = "商户门店信息-非必填")
    private WxPaySceneDetailInfoDTO store_info;
}
