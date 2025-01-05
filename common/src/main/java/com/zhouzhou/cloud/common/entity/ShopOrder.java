package com.zhouzhou.cloud.common.entity;

import com.zhouzhou.cloud.common.service.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-01-05
 * @Description: 订单主表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShopOrder extends BaseEntity {

    private static final long serialVersionUID = 2345782356476235764L;

    /**
     * 订单号
     */
    private String orderCode;

    /**
     * 关联微信订单号
     */
    private String wxOrderCode;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 关联优惠券编码
     */
    private String connectCouponCode;
}