package com.zhouzhou.cloud.common.enums.orderPay;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 商品首页推荐策略枚举
 */
@Getter
@AllArgsConstructor
public enum RecommendTypeEnum {

    CART_RECOMMEND("CART_RECOMMEND", "购物车推荐"),

    BROWSE_RECOMMEND("BROWSE_RECOMMEND", "浏览推荐"),

    COLLECT_RECOMMEND("COLLECT_RECOMMEND", "收藏推荐"),

    PURCHASE_RECOMMEND("PURCHASE_RECOMMEND", "购买推荐"),

    COLLABORATIVE_FILTERING_RECOMMEND("COLLABORATIVE_FILTERING_RECOMMEND", "协同过滤算法推荐");

    /**
     * key
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;

    public static RecommendTypeEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (RecommendTypeEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }

}
