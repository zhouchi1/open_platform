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


    private static final long serialVersionUID = 6786876784523846287L;

    /**
     * 关联订单号
     */
    private String orderCode;

    /**
     * 商品编码
     */
    private String orderGoodsCode;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品数量
     */
    private Integer goodsNum;
}