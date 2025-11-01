package com.zhouzhou.cloud.orderservice.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zhouzhou.cloud.common.resp.BaseStringResp;
import com.zhouzhou.cloud.common.service.base.ResponseData;
import com.zhouzhou.cloud.common.utils.ResponseDataUtil;
import com.zhouzhou.cloud.orderservice.req.CreateOrderReq;
import com.zhouzhou.cloud.orderservice.service.OrderAddressService;
import com.zhouzhou.cloud.orderservice.service.OrderInfoService;
import com.zhouzhou.cloud.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-01-13
 * @Description: 商城订单
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private OrderAddressService orderAddressService;


    @Operation(description = "获取订单列表")
    @ApiOperationSupport(order = 1,author = "Sr.Zhou")
    @PostMapping("/orderList")
    ResponseData<?> getOrderInfoByUserCode(){
        return ResponseDataUtil.success();
    }

    @Operation(description = "获取订单详情")
    @ApiOperationSupport(order = 2,author = "Sr.Zhou")
    @PostMapping("/orderDetails")
    ResponseData<?> getOrderInfoDetailByOrderCode(){
        return ResponseDataUtil.success();
    }

    @Operation(description = "创建订单")
    @ApiOperationSupport(order = 3,author = "Sr.Zhou")
    @PostMapping("/createOrder")
    ResponseData<?> createOrder(@Validated @RequestBody CreateOrderReq createOrderReq) throws Exception {
        orderService.createOrder(createOrderReq);
        return ResponseDataUtil.success();
    }

    @Operation(description = "取消订单")
    @ApiOperationSupport(order = 4,author = "Sr.Zhou")
    @PostMapping("/cancelOrder")
    ResponseData<?> cancelOrder(){
        return ResponseDataUtil.success();
    }

    @Operation(description = "修改订单")
    @ApiOperationSupport(order = 5,author = "Sr.Zhou")
    @PostMapping("/fixOrder")
    ResponseData<?> fixOrder(){
        return ResponseDataUtil.success();
    }

    @Operation(description = "删除订单")
    @ApiOperationSupport(order = 6,author = "Sr.Zhou")
    @PostMapping("/deleteOrder")
    ResponseData<?> deleteOrder(){
        return ResponseDataUtil.success();
    }

    @Operation(description = "获取用户收货地址")
    @ApiOperationSupport(order = 7,author = "Sr.Zhou")
    @PostMapping("/getUserAddress")
    ResponseData<BaseStringResp> getUserAddress(){
//        return ResponseDataUtil.success(orderAddressService.getAddressInfo());
        return null;
    }
}
