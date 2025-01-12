package com.zhouzhou.cloud.orderservice.dubboprovider;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhouzhou.cloud.common.dubboresp.OrderInfoResp;
import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.common.entity.ShopOrderDetails;
import com.zhouzhou.cloud.common.service.interfaces.WxPayOrderService;
import com.zhouzhou.cloud.orderservice.mapper.ShopOrderDetailsMapper;
import com.zhouzhou.cloud.orderservice.mapper.ShopOrderMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 支付所需信息服务类
 */
@Service(version = "1.0.0")
public class WxPayOrderServiceImplV1 implements WxPayOrderService {

    @Resource
    private ShopOrderMapper shopOrderMapper;

    @Resource
    private ShopOrderDetailsMapper shopOrderDetailsMapper;

    @Override
    public OrderInfoResp getOrderInfoByOutTradeNo(String outTradeNo) {
        // 查询订单主表
        ShopOrder shopOrder = shopOrderMapper.selectOne(new LambdaQueryWrapper<ShopOrder>().eq(ShopOrder::getOrderCode, outTradeNo));
        if (shopOrder == null) {
            return null;
        }
        OrderInfoResp orderInfoResp = new OrderInfoResp();
        orderInfoResp.setShopOrder(shopOrder);
        // 查询订单详情信息
        List<ShopOrderDetails> shopOrderDetailsList = shopOrderDetailsMapper.selectList(new LambdaQueryWrapper<ShopOrderDetails>()
                .eq(ShopOrderDetails::getOrderCode, outTradeNo));

        if (CollectionUtils.isEmpty(shopOrderDetailsList)){
            orderInfoResp.setShopOrderDetailsList(null);
        }else{
            orderInfoResp.setShopOrderDetailsList(shopOrderDetailsList);
        }

        return orderInfoResp;
    }

    @Override
    public OrderInfoResp getOrderInfoByTransactionId(String transactionId) {
        // 查询订单主表
        ShopOrder shopOrder = shopOrderMapper.selectOne(new LambdaQueryWrapper<ShopOrder>().eq(ShopOrder::getWxOrderCode, transactionId));
        if (shopOrder == null) {
            return null;
        }
        OrderInfoResp orderInfoResp = new OrderInfoResp();
        orderInfoResp.setShopOrder(shopOrder);
        // 查询订单详情信息
        List<ShopOrderDetails> shopOrderDetailsList = shopOrderDetailsMapper.selectList(new LambdaQueryWrapper<ShopOrderDetails>()
                .eq(ShopOrderDetails::getOrderCode, shopOrder.getOrderCode()));

        if (CollectionUtils.isEmpty(shopOrderDetailsList)){
            orderInfoResp.setShopOrderDetailsList(null);
        }else{
            orderInfoResp.setShopOrderDetailsList(shopOrderDetailsList);
        }

        return orderInfoResp;
    }

    @Override
    public void modifyOrderInfo(OrderInfoResp orderInfoResp) {

    }
}
