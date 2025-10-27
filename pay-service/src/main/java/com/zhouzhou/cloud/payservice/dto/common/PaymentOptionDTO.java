package com.zhouzhou.cloud.payservice.dto.common;

import com.zhouzhou.cloud.payservice.dto.bankcard.BankCardDTO;
import com.zhouzhou.cloud.payservice.dto.promotion.PromotionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOptionDTO implements Serializable {

    private static final Long serialVersionUID = 6487623874621390864L;

    private String id;

    private String type;

    private String name;

    private String icon;

    private String description;

    private List<BankCardDTO> supportedCards;

    private List<PromotionDTO> promotions;

    private Boolean isRecommended;

    private Integer sortOrder;
}
