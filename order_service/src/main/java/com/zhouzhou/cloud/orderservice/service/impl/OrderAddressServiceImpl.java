package com.zhouzhou.cloud.orderservice.service.impl;

import com.zhouzhou.cloud.common.service.interfaces.OrderAddressServiceApi;
import com.zhouzhou.cloud.orderservice.service.OrderAddressService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class OrderAddressServiceImpl implements OrderAddressService {

    // version：调用的微服务版本号
    // loadbalance：负载均衡策略 1、random：随机；2、roundrobin：轮训；3、consistentHash：一致性哈希；4、leastActive：最小活跃度
    @Reference(version = "1.0.0", loadbalance = "leastActive")
    private OrderAddressServiceApi orderAddressServiceApi;

    @Override
    public String getAddressInfo() {
        return orderAddressServiceApi.getAddressInfo();
    }
}
