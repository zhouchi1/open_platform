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
public class BankCards extends BaseEntity {

    private String cardId;

    private String bankName;

    private String cardType;

    /**
     * 脱敏后的卡号
     */
    private String cardNumber;

    private String bankIcon;

    private BigDecimal singleLimit;

    private BigDecimal dailyLimit;
}
