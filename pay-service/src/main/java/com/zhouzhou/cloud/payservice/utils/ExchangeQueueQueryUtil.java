package com.zhouzhou.cloud.payservice.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-1-24
 * @Description: 获取消息队列配置
 */
@Data
@Component
@RefreshScope
public class ExchangeQueueQueryUtil {

    /**
     * 库存扣减交换机名称
     */
    @Value("${warehouse.exchangeName}")
    private String warehouseExchangeName;

    /**
     * 库存扣减路由键名称
     */
    @Value("${warehouse.routeKey}")
    private String warehouseRouteKey;
}
