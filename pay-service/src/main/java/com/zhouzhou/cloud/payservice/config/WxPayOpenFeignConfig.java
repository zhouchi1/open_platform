//package com.zhouzhou.cloud.payservice.config;
//
//
//import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
//import com.wechat.pay.contrib.apache.httpclient.auth.*;
//import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
//import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
//import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
//import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
//import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayConfigDTO;
//import com.zhouzhou.cloud.payservice.utils.WxUtil;
//import feign.Client;
//import feign.httpclient.ApacheHttpClient;
//import jakarta.annotation.Resource;
//import org.apache.http.client.HttpClient;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.springframework.context.annotation.Bean;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.security.GeneralSecurityException;
//import java.security.PrivateKey;
//
///**
// * @Author: Sr.Zhou
// * @CreateTime: 2024-11-29
// * @Description: 微信支付OpenFeign配置类
// */
//public class WxPayOpenFeignConfig {
//
//    @Resource
//    private WxUtil wxUtil;
//
//    @Bean
//    public CloseableHttpClient httpClient() throws GeneralSecurityException, IOException, HttpCodeException, NotFoundException {
//
//        // 获取微信授权信息
//        WxPayConfigDTO wxPayConfigDTO = wxUtil.wxPayConfigQuery();
//        String mchId = wxPayConfigDTO.getSp_mch_id();
//        String wxMerchantKey = wxPayConfigDTO.getMerchant_private_key();
//        String wxSerialNo = wxPayConfigDTO.getSerial_no();
//        String wxApiV3Key = wxPayConfigDTO.getApi_v3_key();
//
//        assert wxMerchantKey != null;
//        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(wxMerchantKey.getBytes(StandardCharsets.UTF_8)));
//        // 获取证书管理器实例
//        CertificatesManager certificatesManager = CertificatesManager.getInstance();
//        // 向证书管理器增加需要自动更新平台证书的商户信息
//        assert wxApiV3Key != null;
//        certificatesManager.putMerchant(mchId, new WechatPay2Credentials(mchId,
//                new PrivateKeySigner(wxSerialNo, merchantPrivateKey)), wxApiV3Key.getBytes(StandardCharsets.UTF_8));
//        // 从证书管理器中获取验签器
//        Verifier verifier = certificatesManager.getVerifier(mchId);
//        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create().withMerchant(mchId, wxSerialNo, merchantPrivateKey)
//                .withValidator(new WechatPay2Validator(verifier));
//        return builder.build();
//    }
//
//    @Bean
//    public Client feignClient(HttpClient httpClient) {
//        return new ApacheHttpClient(httpClient);
//    }
//}
