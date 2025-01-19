package com.zhouzhou.cloud.addressservice.blockhanlder;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Component;

@Component
public class OrderAddressServiceApiBlockHandler {

    /**
     * 处理 BlockException 的方法
     *
     * @return 处理后的结果
     */
    public static String getAddressInfoBlockHandler(BlockException ex) {
        // 处理异常逻辑
        return "系统繁忙，请稍后再试: 北京市朝阳区";
    }
}
