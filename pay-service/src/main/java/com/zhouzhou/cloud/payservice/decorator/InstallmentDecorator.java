package com.zhouzhou.cloud.payservice.decorator;

import com.zhouzhou.cloud.common.entity.Promotion;
import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.payservice.dto.common.PaymentOptionDTO;
import com.zhouzhou.cloud.payservice.dto.promotion.PromotionDTO;
import com.zhouzhou.cloud.payservice.strategy.PaymentStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InstallmentDecorator extends PaymentStrategyDecorator {

    public InstallmentDecorator(PaymentStrategy strategy, Promotion promotion) {
        super(strategy, promotion);
    }

    @Override
    protected ShopOrder applyPromotion(ShopOrder order) {
        // 分期不需要修改订单金额，但需要记录分期信息
        return order; // 实际支付时分期逻辑在银行端处理
    }

    @Override
    protected boolean isPromotionEligible(UserInfo user, ShopOrder order) {
        // 检查订单金额是否达到分期门槛
        if (promotion.getConditionAmount() != null) {
            if (order.getTotalAmount().compareTo(promotion.getConditionAmount()) < 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void enhancePaymentOption(PaymentOptionDTO dto, UserInfo user, ShopOrder order) {
        // 计算每期金额
        BigDecimal installmentAmount = calculateInstallmentAmount(order);

        String periodText = promotion.getInstallmentPeriods() + "期";
        if (promotion.getInstallmentFeeRate().compareTo(BigDecimal.ZERO) == 0) {
            periodText += "免息";
        }

        dto.setName("分期支付 - " + periodText);
        dto.setDescription("每期" + installmentAmount + "元");
        dto.setIsRecommended(true);

        // 添加分期优惠信息
        PromotionDTO promotionDTO = convertToPromotionDTO();
        promotionDTO.setIsEligible(true);

        boolean exists = dto.getPromotions().stream()
                .anyMatch(p -> p.getPromotionId().equals(promotionDTO.getPromotionId()));
        if (!exists) {
            dto.getPromotions().add(promotionDTO);
        }
    }

    private BigDecimal calculateInstallmentAmount(ShopOrder order) {
        BigDecimal totalAmount = order.getTotalAmount();
        if (promotion.getInstallmentFeeRate().compareTo(BigDecimal.ZERO) > 0) {
            // 计算手续费
            BigDecimal fee = totalAmount.multiply(promotion.getInstallmentFeeRate());
            totalAmount = totalAmount.add(fee);
        }

        // 计算每期金额
        return totalAmount.divide(
                new BigDecimal(promotion.getInstallmentPeriods()),
                2, RoundingMode.HALF_UP
        );
    }

    private PromotionDTO convertToPromotionDTO() {
        PromotionDTO dto = new PromotionDTO();
        dto.setPromotionId(promotion.getPromotionId());
        dto.setPromotionName(promotion.getPromotionName());
        dto.setPromotionType(promotion.getPromotionType());
        dto.setDescription(promotion.getDescription());
        dto.setInstallmentPeriods(promotion.getInstallmentPeriods());
        dto.setInstallmentFeeRate(promotion.getInstallmentFeeRate());
        dto.setConditionAmount(promotion.getConditionAmount());
        return dto;
    }
}
