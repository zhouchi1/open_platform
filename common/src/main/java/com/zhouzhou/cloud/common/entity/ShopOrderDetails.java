package com.zhouzhou.cloud.common.entity;

import java.math.BigDecimal;

import com.zhouzhou.cloud.common.service.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-01-05
 * @Description: 订单详情表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShopOrderDetails extends BaseEntity {

    /**
     * 关联订单号
     */
    private String orderId;

    /**
     * 关联商品id
     */
    private Long skuId;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 商品小计
     */
    private BigDecimal subTotal;

    /**
     * 商品状态（已发货/未发货）
     */
    private String status;
}