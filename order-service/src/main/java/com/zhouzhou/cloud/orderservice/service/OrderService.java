package com.zhouzhou.cloud.orderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.orderservice.req.CreateOrderReq;

public interface OrderService extends IService<ShopOrder> {

    void createOrder(CreateOrderReq createOrderReq) throws Exception;
}
