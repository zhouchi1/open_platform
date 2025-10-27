package com.zhouzhou.cloud.payservice.openfeign.wx;

import com.zhouzhou.cloud.payservice.dto.wxlogin.WxMobileGetDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 微信调用接口
 */
@FeignClient(name = "wx-service", url = "https://api.weixin.qq.com")
public interface WxOpenFeignApi {

    /**
     * 获取手机号
     * @param accessToken 授权token
     */
    @PostMapping("/wxa/business/getuserphonenumber?access_token={access_token}")
    String getPhoneNumber(@PathVariable("access_token") String accessToken, @RequestBody WxMobileGetDTO wxMobileDTO);

    /**
     * 获取accessToken
     * @param appId openId
     * @param secret 密钥
     */
    @GetMapping("/cgi-bin/token?appid={app_id}&secret={secret}&grant_type=client_credential")
    String getAccessToken(@PathVariable("app_id") String appId, @PathVariable("secret") String secret);

    /**
     * 获取openId、unionId
     * @param appId openId
     * @param secret 密钥
     * @param jsCode 登录凭证code
     */
    @GetMapping("/sns/jscode2session?appid={app_id}&secret={secret}&js_code={js_code}&grant_type=authorization_code")
    String getOpenId(@PathVariable("app_id") String appId, @PathVariable("secret") String secret, @PathVariable("js_code") String jsCode);
}
