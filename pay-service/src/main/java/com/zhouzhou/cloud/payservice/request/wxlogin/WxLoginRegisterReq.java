package com.zhouzhou.cloud.payservice.request.wxlogin;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "用户code")
    private String code;

    @NotNull(message = "手机号不能为空")
    @Schema(name = "手机号")
    private String userPhone;

    @NotNull(message = "手机验证码不能为空")
    @Schema(name = "手机验证码")
    private String userPhoneCaptcha;

    @NotNull(message = "用户名不能为空")
    @Schema(name = "用户名称")
    private String userName;

    @NotNull(message = "用户密码不能为空")
    @Schema(name = "用户密码")
    private String userPassword;
}
