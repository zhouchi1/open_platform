package com.zhouzhou.cloud.payservice.dto.wxlogin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 微信小程序返回登录手机号信息(水印)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMobileWaterMarkDTO implements Serializable {

    private String timestamp;

    private String appid;
}
