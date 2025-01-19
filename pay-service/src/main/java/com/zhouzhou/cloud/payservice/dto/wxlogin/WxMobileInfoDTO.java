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
public class WxMobileInfoDTO implements Serializable {

    private static final long serialVersionUID = 6782364872354752312L;

    private String phoneNumber;

    private String purePhoneNumber;

    private String countryCode;

    private WxMobileWaterMarkDTO wxMobileWaterMarkDTO;
}
