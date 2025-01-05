package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-04
 * @Description: 微信支付提交支付用户信息（请注意 :下面的两个参数必填其中的一个！！！）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayPayerDTO implements Serializable {

    private static final Long serialVersionUID = 7498237459827398572L;

//    @ApiModelProperty("用户在此商户下的唯一openId标识")
//    private String sp_openid;

    @ApiModelProperty("用户在此子商户下的唯一openId标识")
    private String sub_openid;
}
