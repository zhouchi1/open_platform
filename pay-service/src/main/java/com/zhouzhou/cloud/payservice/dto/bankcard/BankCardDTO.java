package com.zhouzhou.cloud.payservice.dto.bankcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankCardDTO implements Serializable {

    private static final Long serialVersionUID = 6487623874621338764L;

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
