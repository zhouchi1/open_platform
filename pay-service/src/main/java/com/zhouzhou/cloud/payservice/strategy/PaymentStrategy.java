package com.zhouzhou.cloud.payservice.strategy;

import com.zhouzhou.cloud.common.entity.BankCards;
import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.payservice.response.wxpay.PaymentResult;
import com.zhouzhou.cloud.payservice.dto.common.PaymentOptionDTO;

import java.util.List;

public interface PaymentStrategy {
    /**
     * 执行支付
     */
    PaymentResult pay(ShopOrder order, String cardId, String promotionId);

    /**
     * 检查支付方式是否可用
     */
    boolean isAvailable(UserInfo user, ShopOrder order);

    /**
     * 获取支付选项信息
     */
    PaymentOptionDTO getPaymentOption(UserInfo user, ShopOrder order);

    /**
     * 获取支持的银行卡列表
     */
    List<BankCards> getSupportedCards(UserInfo user);

    /**
     * 获取支付方式类型
     */
    String getPaymentType();
}
