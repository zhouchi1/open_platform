package com.zhouzhou.cloud.payservice.service;

import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayOrderDTO;
import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayReFoundStatusDTO;
import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayStatusDTO;
import com.zhouzhou.cloud.payservice.request.pay.wxpay.WxPayCallBackReq;
import com.zhouzhou.cloud.payservice.response.wxpay.WxPayCallBackResp;
import com.zhouzhou.cloud.payservice.response.wxpay.WxPayPrePayInformationResp;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付服务层
 */
public interface WxPayService {

    /**
     * 微信支付预支付请求
     * @param wxPayOrderDTO 微信支付预请求订单信息
     */
    WxPayPrePayInformationResp wxPayPreRequest(WxPayOrderDTO wxPayOrderDTO);

    /**
     * 微信支付订单查询
     * @param str 微信订单号
     * @return 微信支付订单查询结果
     */
    WxPayStatusDTO wxPayStatusQuery(String str);


    /**
     * 微信支付订单退款查询
     * @param str 每日橙订单号
     * @return 微信支付订单退款查询结果
     */
    WxPayReFoundStatusDTO wxPayReFoundStatusQuery(String str);

    /**
     * 接收微信支付回调
     * @param wxPayCallBackReq 请求体
     * @return 微信支付通知响应
     */
    WxPayCallBackResp wxPayCallback(WxPayCallBackReq wxPayCallBackReq);

    /**
     * 接收微信退款回调
     * @param wxPayCallBackReq 请求体
     * @return 微信支付退款通知响应
     */
    WxPayCallBackResp wxPayReFoundCallback(WxPayCallBackReq wxPayCallBackReq);
}
