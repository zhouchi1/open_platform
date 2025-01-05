package com.zhouzhou.cloud.orderservice.dubboprovider;


import com.zhouzhou.cloud.common.dubboresp.OrderInfoResp;
import com.zhouzhou.cloud.common.service.interfaces.WxPayOrderService;
import org.apache.dubbo.config.annotation.Service;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付服务实现层
 */
@Service
public class WxPayOrderServiceImpl implements WxPayOrderService {

    @Override
    public OrderInfoResp getOrderInfoByOutTradeNo(String outTradeNo) {
        return null;
    }

    @Override
    public OrderInfoResp getOrderInfoByTransactionId(String transactionId) {
        return null;
    }

    @Override
    public void modifyOrderInfo(OrderInfoResp orderInfoResp) {

    }
}
