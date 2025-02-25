package com.zhouzhou.cloud.orderservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouzhou.cloud.common.entity.ShopOrder;
import com.zhouzhou.cloud.common.enums.orderPay.WxPayTradeStateEnum;
import com.zhouzhou.cloud.common.service.interfaces.OrderInventoryServiceApi;
import com.zhouzhou.cloud.common.utils.SnowflakeIdGeneratorUtil;
import com.zhouzhou.cloud.orderservice.mapper.ShopOrderMapper;
import com.zhouzhou.cloud.orderservice.req.CreateOrderReq;
import com.zhouzhou.cloud.orderservice.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-01-13
 * @Description: 商城订单服务
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrder>
        implements OrderService {

    @DubboReference(version = "1.0.0")
    private OrderInventoryServiceApi orderInventoryServiceApi;

    @Override
    @GlobalTransactional(name = "order-service", rollbackFor = Exception.class)
    public void createOrder(CreateOrderReq createOrderReq) throws Exception {

        // 分布式雪花算法Id - 基于数据中心Id + 机器Id
        long snowFlakeId = SnowflakeIdGeneratorUtil.createWithAutoConfig().nextId();

        // 根据所提供的商品Id以及购买数量信息 来查询具体的商品信息【Dubbo调用商品服务】 TODO 商品信息查询未完成
//        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>(createOrderReq.getOrderDetailList().size());

        // 创建订单信息
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setId(snowFlakeId);
        shopOrder.setUserId(20011130L);
        shopOrder.setOrderStatus(WxPayTradeStateEnum.NOTPAY.getCode());
        shopOrder.setTotalAmount(BigDecimal.valueOf(100));
        shopOrder.setPaymentAmount(BigDecimal.valueOf(100));
        shopOrder.setShippingAddress("山东");
        shopOrder.setCreateNo("周驰");
        shopOrder.setCreateTime(LocalDateTime.now());
        shopOrder.setUpdateTime(LocalDateTime.now());
        shopOrder.setMark(true);
        baseMapper.insert(shopOrder);

        // 【Dubbo调用库存扣减服务】
        orderInventoryServiceApi.deductInventory();
    }
}
