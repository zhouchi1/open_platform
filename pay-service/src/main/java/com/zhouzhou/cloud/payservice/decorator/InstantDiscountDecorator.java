package com.zhouzhou.cloud.payservice.decorator;

import com.zhouzhou.cloud.common.entity.Promotion;
import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.payservice.dto.common.PaymentOptionDTO;
import com.zhouzhou.cloud.payservice.dto.promotion.PromotionDTO;
import com.zhouzhou.cloud.payservice.strategy.PaymentStrategy;

public class InstantDiscountDecorator extends PaymentStrategyDecorator {

    public InstantDiscountDecorator(PaymentStrategy strategy, Promotion promotion) {
        super(strategy, promotion);
    }

    @Override
    protected ShopOrder applyPromotion(ShopOrder order) {
        // 创建新的订单对象，应用立减优惠
        return new ShopOrder();
    }

    @Override
    protected boolean isPromotionEligible(UserInfo user, ShopOrder order) {
        // 检查订单金额是否满足立减条件
        if (promotion.getConditionAmount() != null) {
            if (order.getTotalAmount().compareTo(promotion.getConditionAmount()) < 0) {
                return false;
            }
        }

        // 检查用户身份条件（如新用户专享）
        if (promotion.getPromotionName().contains("新用户") && !user.getIsNewUser()) {
            return false;
        }

        return true;
    }

    @Override
    protected void enhancePaymentOption(PaymentOptionDTO dto, UserInfo user, ShopOrder order) {
        // 在支付选项名称后添加优惠信息
        String originalName = dto.getName();
        dto.setName(originalName + " (" + promotion.getDescription() + ")");

        // 添加优惠标识
        dto.setIsRecommended(true);

        // 在优惠列表中突出显示
        PromotionDTO promotionDTO = convertToPromotionDTO();
        promotionDTO.setIsEligible(true);

        // 如果列表中还没有这个优惠，则添加
        boolean exists = dto.getPromotions().stream()
                .anyMatch(p -> p.getPromotionId().equals(promotionDTO.getPromotionId()));
        if (!exists) {
            dto.getPromotions().add(0, promotionDTO);
        }
    }

    private PromotionDTO convertToPromotionDTO() {
        PromotionDTO dto = new PromotionDTO();
        dto.setPromotionId(promotion.getPromotionId());
        dto.setPromotionName(promotion.getPromotionName());
        dto.setPromotionType(promotion.getPromotionType());
        dto.setDescription(promotion.getDescription());
        dto.setDiscountAmount(promotion.getDiscountAmount());
        dto.setConditionAmount(promotion.getConditionAmount());
        return dto;
    }
}
