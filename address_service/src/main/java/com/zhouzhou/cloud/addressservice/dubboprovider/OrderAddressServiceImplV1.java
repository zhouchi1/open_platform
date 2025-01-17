package com.zhouzhou.cloud.addressservice.dubboprovider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zhouzhou.cloud.common.service.interfaces.OrderAddressServiceApi;
import org.apache.dubbo.config.annotation.Service;

@Service(version = "1.0.0")
public class OrderAddressServiceImplV1 implements OrderAddressServiceApi {

    @Override
    @SentinelResource(
            value = "getAddressInfo",
            blockHandler = "getAddressInfoBlockHandler",
            fallback = "getAddressInfoFallback"
    )
    public String getAddressInfo() {
        return "山东省济南市历下区丁家融汇花园";
    }

    public String getAddressInfoBlockHandler(BlockException ex) {
        return "系统繁忙，请稍后再试: 北京市朝阳区";
    }

    public String getAddressInfoFallback(Throwable throwable) {
        return "默认地址: 北京市朝阳区";
    }
}
