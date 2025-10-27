package com.zhouzhou.cloud.payservice.strategy;

import com.zhouzhou.cloud.common.entity.BankCards;
import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.payservice.dto.bankcard.BankCardDTO;
import com.zhouzhou.cloud.payservice.dto.common.PaymentOptionDTO;
import com.zhouzhou.cloud.payservice.dto.promotion.PromotionDTO;
import com.zhouzhou.cloud.payservice.response.wxpay.PaymentResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
public class CreditCardStrategy implements PaymentStrategy{

    @Override
    public PaymentResult pay(ShopOrder order, String cardId, String promotionId) {
        // 模拟信用卡支付逻辑
        try {
            // 1. 验证卡片状态
            // 2. 调用银行支付接口
            // 3. 处理支付结果
            log.info("执行信用卡支付，订单：" + order.getId() +
                    ", 卡片：" + cardId + ", 优惠：" + promotionId);

            // 模拟支付处理
            Thread.sleep(100);

            // 返回支付成功结果
            return PaymentResult.success("TX" + System.currentTimeMillis());
        } catch (Exception e) {
            return PaymentResult.failure("信用卡支付失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isAvailable(UserInfo user, ShopOrder order) {

        // 检查用户是否绑定信用卡

        // 检查订单金额是否在限额内

        return true;
    }

    @Override
    public PaymentOptionDTO getPaymentOption(UserInfo user, ShopOrder order) {
        PaymentOptionDTO dto = new PaymentOptionDTO();
        dto.setId("credit_card");
        dto.setType("CREDIT_CARD");
        dto.setName("信用卡支付");
        dto.setIcon("/icons/credit-card.png");
        dto.setDescription("支持各大银行信用卡");
        dto.setIsRecommended(true);
        dto.setSortOrder(1);

        // 设置支持的银行卡
        List<BankCardDTO> cardDTOs = getSupportedCards(user).stream()
                .map(this::convertToBankCardDTO)
                .collect(Collectors.toList());
        dto.setSupportedCards(cardDTOs);

        // 设置优惠活动（这里应该从优惠服务获取）
        dto.setPromotions(getAvailablePromotions(user, order));

        return dto;
    }

    private BankCardDTO convertToBankCardDTO(BankCards card) {
        BankCardDTO dto = new BankCardDTO();
        dto.setCardId(card.getCardId());
        dto.setBankName(card.getBankName());
        dto.setCardType(card.getCardType());
        dto.setCardNumber("**** **** **** " + card.getCardNumber().substring(card.getCardNumber().length() - 4));
        dto.setBankIcon(card.getBankIcon());
        dto.setSingleLimit(card.getSingleLimit());
        dto.setDailyLimit(card.getDailyLimit());
        return dto;
    }

    @Override
    public List<BankCards> getSupportedCards(UserInfo user) {
        // 返回用户绑定的信用卡
        return null;
    }

    private List<PromotionDTO> getAvailablePromotions(UserInfo user, ShopOrder order) {
        List<PromotionDTO> promotions = new ArrayList<>();

        // 模拟获取信用卡相关优惠
        if (order.getTotalAmount().compareTo(new java.math.BigDecimal("100")) >= 0) {
            PromotionDTO discountPromo = new PromotionDTO();
            discountPromo.setPromotionId("promo_credit_001");
            discountPromo.setPromotionName("信用卡满100减5");
            discountPromo.setPromotionType("INSTANT_DISCOUNT");
            discountPromo.setDescription("使用信用卡支付满100元立减5元");
            discountPromo.setDiscountAmount(new java.math.BigDecimal("5"));
            discountPromo.setConditionAmount(new java.math.BigDecimal("100"));
            discountPromo.setIsEligible(true);
            promotions.add(discountPromo);
        }

        // 分期优惠
        if (order.getTotalAmount().compareTo(new java.math.BigDecimal("500")) >= 0) {
            PromotionDTO installmentPromo = new PromotionDTO();
            installmentPromo.setPromotionId("promo_credit_002");
            installmentPromo.setPromotionName("3期免息");
            installmentPromo.setPromotionType("INSTALLMENT_FREE");
            installmentPromo.setDescription("支持3期免息分期");
            installmentPromo.setInstallmentPeriods(3);
            installmentPromo.setInstallmentFeeRate(java.math.BigDecimal.ZERO);
            installmentPromo.setIsEligible(true);
            promotions.add(installmentPromo);
        }

        return promotions;
    }

    @Override
    public String getPaymentType() {
        return "CREDIT_CARD";
    }
}
