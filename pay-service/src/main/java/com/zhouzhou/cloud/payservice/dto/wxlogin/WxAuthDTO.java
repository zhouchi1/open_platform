package com.zhouzhou.cloud.payservice.dto.wxlogin;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name ="用户openId")
    private String openid;

    @Schema(name ="会话密钥")
    private String sessionKey;

    @Schema(name ="用户在开放平台的唯一标识符")
    private String unionid;

    @Schema(name ="小程序全局唯一后台接口调用凭据")
    private String accessToken;

    @Schema(name ="凭证有效时间，单位：秒。目前是7200秒之内的值。")
    private String expiresIn;

    @Schema(name ="手机信息")
    private String phoneNumber;

    @Schema(name ="错误码")
    private String errcode;

    @Schema(name ="错误信息")
    private String errmsg;
}
