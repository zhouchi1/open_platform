package com.zhouzhou.cloud.addressservice.dubboprovider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zhouzhou.cloud.addressservice.blockhanlder.OrderAddressServiceApiBlockHandler;
import com.zhouzhou.cloud.addressservice.fallback.OrderAddressServiceApiFallBack;
import com.zhouzhou.cloud.common.service.interfaces.OrderAddressServiceApi;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = "1.0.0")
public class OrderAddressServiceImplV1 implements OrderAddressServiceApi {

    @Override
    @SentinelResource(
            value = "getAddressInfo",
            blockHandlerClass = OrderAddressServiceApiBlockHandler.class,
            blockHandler = "getAddressInfoBlockHandler",
            fallbackClass = OrderAddressServiceApiFallBack.class,
            fallback = "getAddressInfoFallback"
    )
    public String getAddressInfo() {
        return "山东省济南市历下区丁家融汇花园";
    }
}
