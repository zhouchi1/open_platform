package com.zhouzhou.cloud.payservice.dto.wxlogin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 微信小程序返回登录手机号信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMobileResultDTO implements Serializable {

    private static final long serialVersionUID = 4628736487236487231L;

    private Integer errcode;

    private String errmsg;

    private WxMobileInfoDTO phoneInfo;
}
