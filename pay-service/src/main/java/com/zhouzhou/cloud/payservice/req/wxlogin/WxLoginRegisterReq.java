package com.zhouzhou.cloud.payservice.req.wxlogin;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 微信小程序登录注册请求实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxLoginRegisterReq  {

    @NotNull(message = "code不能为空")
    @ApiModelProperty(value = "用户code", required = true)
    private String code;

    @NotNull(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号", required = true)
    private String userPhone;

    @NotNull(message = "手机验证码不能为空")
    @ApiModelProperty(value = "手机验证码", required = true)
    private String userPhoneCaptcha;

    @NotNull(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名称", required = true)
    private String userName;

    @NotNull(message = "用户密码不能为空")
    @ApiModelProperty(value = "用户密码",required = true)
    private String userPassword;
}
