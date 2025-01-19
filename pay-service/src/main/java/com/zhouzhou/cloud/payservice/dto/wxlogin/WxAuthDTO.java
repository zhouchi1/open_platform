package com.zhouzhou.cloud.payservice.dto.wxlogin;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 微信小程序获取登录用户人信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxAuthDTO implements Serializable {

    private static final long serialVersionUID = 8712654785234765327L;

    @ApiModelProperty("用户openId")
    private String openid;

    @ApiModelProperty("会话密钥")
    private String sessionKey;

    @ApiModelProperty("用户在开放平台的唯一标识符")
    private String unionid;

    @ApiModelProperty("小程序全局唯一后台接口调用凭据")
    private String accessToken;

    @ApiModelProperty("凭证有效时间，单位：秒。目前是7200秒之内的值。")
    private String expiresIn;

    @ApiModelProperty("手机信息")
    private String phoneNumber;

    @ApiModelProperty("错误码")
    private String errcode;

    @ApiModelProperty("错误信息")
    private String errmsg;
}
