package com.zhouzhou.cloud.common.entity;

import com.zhouzhou.cloud.common.service.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-01-05
 * @Description: 订单主表
 */
@Data
//@AllArgsConstructor
//@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShopOrder extends BaseEntity {

    private static final long serialVersionUID = 2345782356476235764L;

    /**
     * 关联用户id
     */
    private Long userId;

    /**
     * 关联微信订单号
     */
    private String wxOrderId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 收货地址
     */
    private String shippingAddress;

    /**
     * 配送方式
     */
    private String shippingMethod;

    /**
     * 发货时间
     */
    private LocalDateTime shippingDate;

    /**
     * 确认收货时间
     */
    private LocalDateTime deliveryDate;

    /**
     * 支付时间
     */
    private LocalDateTime paymentDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 关联优惠券编码
     */
    private String connectCouponCode;
}