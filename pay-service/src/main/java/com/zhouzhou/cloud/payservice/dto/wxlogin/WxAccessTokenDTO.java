package com.zhouzhou.cloud.payservice.dto.wxlogin;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 微信小程序获取accessToken信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxAccessTokenDTO implements Serializable {

    private static final long serialVersionUID = 8723647235476235472L;

    @ApiModelProperty("accessToken")
    private String accessToken;

    @ApiModelProperty("过期时间")
    private String expiresIn;
}
