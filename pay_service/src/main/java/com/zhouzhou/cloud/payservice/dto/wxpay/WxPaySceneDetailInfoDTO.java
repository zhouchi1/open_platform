package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-04
 * @Description: 微信支付场景详情信息（全部非必填）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPaySceneDetailInfoDTO implements Serializable {

    private static final long serialVersionUID = 6472354687236478212L;

    @ApiModelProperty("门店编号")
    private String id;

    @ApiModelProperty("门店名称")
    private String name;

    @ApiModelProperty("地区编码")
    private String area_code;

    @ApiModelProperty("地址")
    private String address;
}
