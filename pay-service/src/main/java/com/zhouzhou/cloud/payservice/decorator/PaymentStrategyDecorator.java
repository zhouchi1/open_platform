package com.zhouzhou.cloud.payservice.decorator;

import com.zhouzhou.cloud.common.entity.BankCards;
import com.zhouzhou.cloud.common.entity.Promotion;
import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.payservice.dto.common.PaymentOptionDTO;
import com.zhouzhou.cloud.payservice.response.wxpay.PaymentResult;
import com.zhouzhou.cloud.payservice.strategy.PaymentStrategy;

import java.math.BigDecimal;
import java.util.List;

public abstract class PaymentStrategyDecorator implements PaymentStrategy {
    protected PaymentStrategy decoratedStrategy;
    protected Promotion promotion;

    public PaymentStrategyDecorator(PaymentStrategy strategy, Promotion promotion) {
        this.decoratedStrategy = strategy;
        this.promotion = promotion;
    }

    @Override
    public PaymentResult pay(ShopOrder order, String cardId, String promotionId) {
        // 应用优惠后再执行支付
        ShopOrder discountedOrder = applyPromotion(order);
        return decoratedStrategy.pay(discountedOrder, cardId, promotionId);
    }

    @Override
    public boolean isAvailable(UserInfo user, ShopOrder order) {
        // 检查基础策略是否可用，并且优惠活动也适用
        return decoratedStrategy.isAvailable(user, order) &&
                isPromotionEligible(user, order);
    }

    @Override
    public PaymentOptionDTO getPaymentOption(UserInfo user, ShopOrder order) {
        PaymentOptionDTO dto = decoratedStrategy.getPaymentOption(user, order);

        // 装饰支付选项，添加优惠信息
        if (isPromotionEligible(user, order)) {
            enhancePaymentOption(dto, user, order);
        }

        return dto;
    }

    @Override
    public List<BankCards> getSupportedCards(UserInfo user) {
        return decoratedStrategy.getSupportedCards(user);
    }

    @Override
    public String getPaymentType() {
        return decoratedStrategy.getPaymentType();
    }

    /**
     * 应用优惠到订单
     */
    protected abstract ShopOrder applyPromotion(ShopOrder order);

    /**
     * 检查优惠活动是否适用
     */
    protected abstract boolean isPromotionEligible(UserInfo user, ShopOrder order);

    /**
     * 增强支付选项显示
     */
    protected abstract void enhancePaymentOption(PaymentOptionDTO dto, UserInfo user, ShopOrder order);

    /**
     * 计算优惠后的金额
     */
    protected BigDecimal calculateDiscountedAmount(ShopOrder order) {
        BigDecimal originalAmount = order.getTotalAmount();
        BigDecimal discount = promotion.getDiscountAmount() != null ?
                promotion.getDiscountAmount() : BigDecimal.ZERO;

        BigDecimal discounted = originalAmount.subtract(discount);
        return discounted.compareTo(BigDecimal.ZERO) > 0 ? discounted : BigDecimal.ZERO;
    }
}
