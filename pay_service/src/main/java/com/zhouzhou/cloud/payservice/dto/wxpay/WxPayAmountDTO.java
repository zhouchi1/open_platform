package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-04
 * @Description: 微信支付金额数据传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayAmountDTO implements Serializable {

    private static final Long serialVersionUID = 7462378467823648762L;

    @ApiModelProperty("微信支付金额")
    private Integer total;

    @ApiModelProperty("微信支付币种")
    private String currency;
}
