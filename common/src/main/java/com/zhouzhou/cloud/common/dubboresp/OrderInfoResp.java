package com.zhouzhou.cloud.common.dubboresp;

import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.common.entity.ShopOrderDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-01-05
 * @Description: 订单信息返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoResp implements Serializable {


    private static final long serialVersionUID = 6427356476235467253L;

    /**
     * 订单信息
     */
    private ShopOrder shopOrder;

    /**
     * 订单详情
     */
    private List<ShopOrderDetails> shopOrderDetailsList;
}
