package com.zhouzhou.cloud.common.service.interfaces;

import com.zhouzhou.cloud.common.dubboresp.OrderInfoResp;

public interface WxPayOrderServiceApi {

    /**
     * 通过商城订单号查询订单信息
     * @param outTradeNo 商城订单号
     * @return 订单信息
     */
    OrderInfoResp getOrderInfoByOutTradeNo(String outTradeNo);

    /**
     * 通过微信订单号查询订单信息
     * @param transactionId 微信订单号
     * @return 订单信息
     */
    OrderInfoResp getOrderInfoByTransactionId(String transactionId);


    /**
     * 修改订单信息
     * @param orderInfoResp 订单信息
     */
    void modifyOrderInfo(OrderInfoResp orderInfoResp);
}
