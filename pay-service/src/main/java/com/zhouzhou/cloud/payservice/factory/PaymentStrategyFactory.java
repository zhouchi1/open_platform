package com.zhouzhou.cloud.payservice.factory;

import com.zhouzhou.cloud.common.entity.Promotion;
import com.zhouzhou.cloud.payservice.decorator.InstallmentDecorator;
import com.zhouzhou.cloud.payservice.decorator.InstantDiscountDecorator;
import com.zhouzhou.cloud.payservice.strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PaymentStrategyFactory {

    @Autowired
    private Map<String, PaymentStrategy> strategyMap;

    /**
     * 根据支付类型获取基础策略
     */
    public PaymentStrategy getBaseStrategy(String paymentType) {
        String beanName = getStrategyBeanName(paymentType);
        return strategyMap.get(beanName);
    }

    /**
     * 为策略添加装饰器（优惠活动）
     */
    public PaymentStrategy decorateStrategy(PaymentStrategy baseStrategy, Promotion promotion) {
        if (baseStrategy == null || promotion == null) {
            return baseStrategy;
        }

        return switch (promotion.getPromotionType()) {
            case "INSTANT_DISCOUNT" -> new InstantDiscountDecorator(baseStrategy, promotion);
            case "INSTALLMENT", "INSTALLMENT_FREE" -> new InstallmentDecorator(baseStrategy, promotion);

            default -> baseStrategy;
        };
    }

    /**
     * 为策略添加多个装饰器
     */
    public PaymentStrategy decorateStrategyWithMultiplePromotions(
            PaymentStrategy baseStrategy, List<Promotion> promotions) {
        PaymentStrategy decoratedStrategy = baseStrategy;

        for (Promotion promotion : promotions) {
            decoratedStrategy = decorateStrategy(decoratedStrategy, promotion);
        }

        return decoratedStrategy;
    }

    private String getStrategyBeanName(String paymentType) {
        return switch (paymentType) {
            case "CREDIT_CARD" -> "creditCardStrategy";
            case "DEBIT_CARD" -> "debitCardStrategy";
            case "INSTALLMENT" -> "installmentStrategy";
            default -> throw new IllegalArgumentException("不支持的支付类型: " + paymentType);
        };
    }
}
