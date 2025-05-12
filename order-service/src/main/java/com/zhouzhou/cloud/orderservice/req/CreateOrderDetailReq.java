package com.zhouzhou.cloud.orderservice.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-01-13
 * @Description: 创建商城订单详情信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDetailReq {

    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量不能小于1")
    @ApiModelProperty("商品数量")
    private Integer quantity;

    @NotNull(message = "商品信息不能为空")
    @ApiModelProperty("商品id")
    private Long skuId;
}
