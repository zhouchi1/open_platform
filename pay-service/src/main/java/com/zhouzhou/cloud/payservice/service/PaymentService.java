package com.zhouzhou.cloud.payservice.service;

import com.zhouzhou.cloud.payservice.dto.common.PaymentOptionDTO;
import com.zhouzhou.cloud.payservice.request.pay.PaymentRequest;
import com.zhouzhou.cloud.payservice.response.wxpay.PaymentResult;

import java.util.List;

public interface PaymentService {

    /**
     * 获取可用的支付选项列表
     */
    List<PaymentOptionDTO> getAvailablePaymentOptions(String orderId);


    /**
     * 执行支付
     */
    PaymentResult executePayment(PaymentRequest paymentRequest);
}
