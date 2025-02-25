package com.zhouzhou.cloud.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-01-05
 * @Description: 查询具体的商品信息 例如价格等
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderDetailDTO implements Serializable {

    private static final long serialVersionUID = 7832648762387462837L;

    /**
     * 商品skuId
     */
    private Long skuId;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 商品单价
     */
    private BigDecimal unitPrice;

    /**
     * 商品小计
     */
    private BigDecimal subTotal;
}
