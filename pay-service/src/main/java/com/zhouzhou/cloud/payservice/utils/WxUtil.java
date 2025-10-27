package com.zhouzhou.cloud.payservice.utils;

import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.zhouzhou.cloud.common.utils.BizExUtil;
import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayConfigDTO;
import com.zhouzhou.cloud.payservice.response.wxpay.WxPayPrePayInformationResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-29
 * @Description: 微信支付工具类
 */
@Component
@RefreshScope
public class WxUtil {


    @Value("${wx.sp.app_id}")
    private String spAppId;

    @Value("${wx.sp.mch_id}")
    private String spMchId;

    @Value("${wx.sub.mch_id}")
    private String subMchId;

    @Value("${wx.sub.app_id}")
    private String subAppId;

    @Value("${wx.applet_secret}")
    private String appletSecret;

    @Value("${wx.pay_url}")
    private String payUrl;

    @Value("${wx.order_query_url}")
    private String orderQueryUrl;

    @Value("${wx.refund_url}")
    private String refundUrl;

    @Value("${wx.refund_query_url}")
    private String refundQueryUrl;

    @Value("${wx.cancel_order_url}")
    private String cancelOrderUrl;

    @Value("${wx.api_v3_key}")
    private String apiV3Key;

    @Value("${wx.merchant_private_key}")
    private String merchantPrivateKey;

    @Value("${wx.serial_no}")
    private String serialNo;

    @Value("${wx.notify_url}")
    private String notifyUrl;

    @Value("${wx.refund_notify_url}")
    private String refundNotifyUrl;


    /**
     * 微信支付获取数字签名-采用RSA加密
     * @param subAppId 子商户APPID
     * @param timeStamp 时间戳
     * @param nonceStr 32位随机字符串
     * @param prepay_id 预支付交易会话标识
     * @param wxMerchantKey 微信商户密钥
     * @return 唤起微信支付所需要的参数
     */
    public WxPayPrePayInformationResp wxSign(String subAppId, String timeStamp, String nonceStr, String prepay_id, String wxMerchantKey) {

        WxPayPrePayInformationResp wxPayPrePayInformationResp = new WxPayPrePayInformationResp();

        StringBuilder builder = new StringBuilder();
        builder.append(subAppId).append("\n").append(timeStamp).append("\n").append(nonceStr).append("\n").append(prepay_id).append("\n");
        try {
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(wxMerchantKey.getBytes(StandardCharsets.UTF_8)));
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(merchantPrivateKey);
            sign.update(builder.toString().getBytes(StandardCharsets.UTF_8));

            wxPayPrePayInformationResp.setPaySign(Base64Utils.encodeToString(sign.sign()));
            wxPayPrePayInformationResp.setPrepay_id(prepay_id);
            wxPayPrePayInformationResp.setNonceStr(nonceStr);
            wxPayPrePayInformationResp.setTimeStamp(timeStamp);
            return wxPayPrePayInformationResp;
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return wxPayPrePayInformationResp;
    }

    /**
     * AES-GCM解密方法
     */
    public String decrypt(String apiV3Key, String ciphertext, String associatedData, String nonce) throws Exception {

        // 将APIv3密钥转为字节数组
        byte[] key = apiV3Key.getBytes(StandardCharsets.UTF_8);
        Key secretKey = new SecretKeySpec(key, "AES");

        // 使用AES-GCM解密
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));

        byte[] decodedCiphertext = Base64.getDecoder().decode(ciphertext);
        byte[] decryptedData = cipher.doFinal(decodedCiphertext);

        return new String(decryptedData, StandardCharsets.UTF_8);
    }



    @SuppressWarnings("Duplicates")
    public WxPayConfigDTO wxPayConfigQuery() {

        // 从配置文件中读取微信支付参数配置
        WxPayConfigDTO wxPayConfigDTO = new WxPayConfigDTO();

        // 检查配置是否为空
        BizExUtil.requirefalse(StringUtils.isEmpty(spAppId), "1-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(spMchId), "2-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(subMchId), "3-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(subAppId), "4-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(appletSecret), "5-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(payUrl), "6-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(orderQueryUrl), "7-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(refundUrl), "8-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(refundQueryUrl), "9-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(cancelOrderUrl), "10-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(apiV3Key), "11-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(merchantPrivateKey), "12-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(serialNo), "13-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(notifyUrl), "14-微信支付配置信息不足,请联系技术人员核验");
        BizExUtil.requirefalse(StringUtils.isEmpty(refundNotifyUrl), "15-微信支付配置信息不足,请联系技术人员核验");

        wxPayConfigDTO.setSp_app_id(spAppId);
        wxPayConfigDTO.setSp_mch_id(spMchId);
        wxPayConfigDTO.setSub_mch_id(subMchId);
        wxPayConfigDTO.setSub_app_id(subAppId);
        wxPayConfigDTO.setApplet_secret(appletSecret);
        wxPayConfigDTO.setPay_url(payUrl);
        wxPayConfigDTO.setOrder_query_url(orderQueryUrl);
        wxPayConfigDTO.setRefund_url(refundUrl);
        wxPayConfigDTO.setRefund_query_url(refundQueryUrl);
        wxPayConfigDTO.setCancel_order_url(cancelOrderUrl);
        wxPayConfigDTO.setApi_v3_key(apiV3Key);
        wxPayConfigDTO.setMerchant_private_key(merchantPrivateKey);
        wxPayConfigDTO.setSerial_no(serialNo);
        wxPayConfigDTO.setNotify_url(notifyUrl);
        wxPayConfigDTO.setRefund_notify_url(refundNotifyUrl);
        return wxPayConfigDTO;
    }

}
