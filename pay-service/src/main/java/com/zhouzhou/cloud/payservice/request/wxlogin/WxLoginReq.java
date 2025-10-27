package com.zhouzhou.cloud.payservice.request.wxlogin;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "用户code")
    private String code;

    @Schema(name = "手机号code")
    private String phoneCode;

    @Schema(name = "手机号")
    private String userPhone;

    @Schema(name = "手机验证码")
    private String userPhoneCaptcha;

    @Schema(name = "用户密码")
    private String userPassword;
}
