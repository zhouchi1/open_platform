package com.zhouzhou.cloud.common.entity;


import com.zhouzhou.cloud.common.service.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Promotion extends BaseEntity {

    private String promotionId;

    private String promotionName;

    private String promotionType; // INSTANT_DISCOUNT, INSTALLMENT_FREE, COUPON, etc.

    private String description;

    private BigDecimal discountAmount;

    private BigDecimal conditionAmount; // 满减条件

    private Integer installmentPeriods; // 分期期数

    private BigDecimal installmentFeeRate; // 分期费率
}