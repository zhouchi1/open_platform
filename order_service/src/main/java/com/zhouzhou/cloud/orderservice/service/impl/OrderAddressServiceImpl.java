package com.zhouzhou.cloud.orderservice.service.impl;

import com.zhouzhou.cloud.common.resp.BaseStringResp;
import com.zhouzhou.cloud.common.service.interfaces.OrderAddressServiceApi;
import com.zhouzhou.cloud.orderservice.service.OrderAddressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderAddressServiceImpl implements OrderAddressService {


    // version：调用的微服务版本号
    // loadbalance：负载均衡策略 1、random：随机；2、roundrobin：轮训；3、consistentHash：一致性哈希；4、leastActive：最小活跃度
    @DubboReference(version = "1.0.0")
    private OrderAddressServiceApi orderAddressServiceApi;

    @Override
    public BaseStringResp getAddressInfo() {
        String addressInfo = orderAddressServiceApi.getAddressInfo();
        log.info(addressInfo);;
        return BaseStringResp.builder().str(addressInfo).build();
    }
}
