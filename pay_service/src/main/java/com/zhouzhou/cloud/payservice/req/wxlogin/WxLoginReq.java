package com.zhouzhou.cloud.payservice.req.wxlogin;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 微信小程序登录前端请求实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxLoginReq {

    private static final long serialVersionUID = 8764871236487236487L;

    @ApiModelProperty(value = "用户code")
    private String code;

    @ApiModelProperty(value = "手机号code")
    private String phoneCode;

    @ApiModelProperty(value = "手机号")
    private String userPhone;

    @ApiModelProperty(value = "手机验证码")
    private String userPhoneCaptcha;

    @ApiModelProperty("用户密码")
    private String userPassword;
}
