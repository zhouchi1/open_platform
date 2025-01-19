package com.zhouzhou.cloud.payservice.dto.wxlogin;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 微信小程序获取登录手机号信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMobileGetDTO implements Serializable {

    private static final long serialVersionUID = 6325476237846238746L;

    @ApiModelProperty("手机获取Code")
    private String code;
}
