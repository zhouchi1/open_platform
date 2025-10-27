package com.zhouzhou.cloud.payservice.dto.promotion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO implements Serializable {

    private static final Long serialVersionUID = 6487628674621338764L;

    private String promotionId;

    private String promotionName;

    private String promotionType;

    private String description;

    private BigDecimal discountAmount;

    private BigDecimal conditionAmount;

    private Integer installmentPeriods;

    private BigDecimal installmentFeeRate;

    private Boolean isEligible;
}
