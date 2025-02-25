package com.zhouzhou.cloud.warehouseservice.dubboprovider;

import com.zhouzhou.cloud.common.service.interfaces.OrderInventoryServiceApi;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcException;

@DubboService(version = "1.0.0")
public class OrderInventoryServiceImplV1 implements OrderInventoryServiceApi {

    @Override
    public void deductInventory() throws Exception {
        throw new RpcException("库存服务调用失败");
    }
}
