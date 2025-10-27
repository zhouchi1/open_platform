package com.zhouzhou.cloud.payservice.service.impl;

import com.zhouzhou.cloud.common.entity.Promotion;
import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.common.service.interfaces.OrderRpcServer;
import com.zhouzhou.cloud.common.service.interfaces.PromotionRpcServer;
import com.zhouzhou.cloud.common.service.interfaces.UserRpcServer;
import com.zhouzhou.cloud.common.utils.BizExUtil;
import com.zhouzhou.cloud.common.utils.LoginUserContextHolder;
import com.zhouzhou.cloud.payservice.dto.common.PaymentOptionDTO;
import com.zhouzhou.cloud.payservice.request.pay.PaymentRequest;
import com.zhouzhou.cloud.payservice.response.wxpay.PaymentResult;
import com.zhouzhou.cloud.payservice.service.PaymentService;
import com.zhouzhou.cloud.payservice.strategy.PaymentStrategy;
import com.zhouzhou.cloud.payservice.factory.PaymentStrategyFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentStrategyFactory strategyFactory;

    @DubboReference(version = "1.0.0")
    private UserRpcServer userRpcServer;

    @DubboReference(version = "1.0.0")
    private PromotionRpcServer promotionRpcServer;

    @DubboReference(version = "1.0.0")
    private OrderRpcServer orderRpcServer;

    @Override
    public List<PaymentOptionDTO> getAvailablePaymentOptions(String orderId) {
        // 获取用户信息和订单信息
        UserInfo user = userRpcServer.getUserById(LoginUserContextHolder.get().getUserResp().getUserId());

        ShopOrder order = orderRpcServer.getOrderById(orderId);

        BizExUtil.requirefalse(ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(order), "用户信息未找到");

        BizExUtil.requirefalse(ObjectUtils.isEmpty(user.getUserName()), "用户未创建用户名");

        BizExUtil.requirefalse(ObjectUtils.isEmpty(order.getCreateNo()), "订单不存在归属人");

        // 用户与单据进行匹配校验
        BizExUtil.requirefalse(!user.getUserName().equals(order.getCreateNo()), "存在风险操作，已拒绝访问");

        // 获取所有基础支付策略
        List<PaymentStrategy> baseStrategies = getBaseStrategies();

        // 获取可用的优惠活动
        List<Promotion> availablePromotions = promotionRpcServer.getAvailablePromotions(user, order);

        // 为每个基础策略创建装饰后的策略
        List<PaymentStrategy> decoratedStrategies = baseStrategies.stream()
                .map(strategy -> {
                    // 获取与该策略匹配的优惠活动
                    List<Promotion> matchedPromotions = filterPromotionsForStrategy(
                            availablePromotions, strategy);
                    // 使用工厂装饰策略
                    return strategyFactory.decorateStrategyWithMultiplePromotions(
                            strategy, matchedPromotions);
                })
                .collect(Collectors.toList());

        // 过滤可用的支付选项并转换为DTO
        return decoratedStrategies.stream()
                .filter(strategy -> strategy.isAvailable(user, order))
                .map(strategy -> strategy.getPaymentOption(user, order))
                .sorted(Comparator.comparing(PaymentOptionDTO::getSortOrder)
                        // 推荐置顶
                        .thenComparing(option -> !option.getIsRecommended()))
                .collect(Collectors.toList());
    }


    @Override
    public PaymentResult executePayment(PaymentRequest paymentRequest) {
        // 获取用户和订单信息
        UserInfo user = userRpcServer.getUserById(LoginUserContextHolder.get().getUserResp().getUserId());
        ShopOrder order = orderRpcServer.getOrderById(paymentRequest.getOrderId());

        if (user == null || order == null) {
            return PaymentResult.failure("用户或订单不存在");
        }

        // 获取基础支付策略
        PaymentStrategy baseStrategy = strategyFactory.getBaseStrategy(paymentRequest.getPaymentType());
        if (baseStrategy == null) {
            return PaymentResult.failure("不支持的支付方式");
        }

        // 如果有优惠活动，装饰策略
        PaymentStrategy finalStrategy = baseStrategy;
        if (paymentRequest.getPromotionId() != null) {
            Promotion promotion = promotionRpcServer.getPromotionById(paymentRequest.getPromotionId());
            if (promotion != null) {
                finalStrategy = strategyFactory.decorateStrategy(baseStrategy, promotion);
            }
        }

        // 检查支付方式是否可用
        if (!finalStrategy.isAvailable(user, order)) {
            return PaymentResult.failure("当前支付方式不可用");
        }

        // 执行支付
        return finalStrategy.pay(order, paymentRequest.getCardId(), paymentRequest.getPromotionId());
    }

    private List<PaymentStrategy> getBaseStrategies() {
        List<PaymentStrategy> strategies = new ArrayList<>();

        // 从工厂获取所有基础策略
        strategies.add(strategyFactory.getBaseStrategy("CREDIT_CARD"));
        strategies.add(strategyFactory.getBaseStrategy("DEBIT_CARD"));
        strategies.add(strategyFactory.getBaseStrategy("INSTALLMENT"));

        return strategies;
    }

    private List<Promotion> filterPromotionsForStrategy(List<Promotion> promotions, PaymentStrategy strategy) {
        return promotions.stream()
                .filter(promotion -> isPromotionCompatibleWithStrategy(promotion, strategy))
                .collect(Collectors.toList());
    }

    private boolean isPromotionCompatibleWithStrategy(Promotion promotion, PaymentStrategy strategy) {
        String paymentType = strategy.getPaymentType();
        String promotionType = promotion.getPromotionType();

        // 分期优惠只与分期策略兼容
        if (promotionType.contains("INSTALLMENT")) {
            return "INSTALLMENT".equals(paymentType);
        }

        // 立减优惠与信用卡、借记卡兼容
        if ("INSTANT_DISCOUNT".equals(promotionType)) {
            return "CREDIT_CARD".equals(paymentType) || "DEBIT_CARD".equals(paymentType);
        }

        return false;
    }
}
