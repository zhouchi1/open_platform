package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-04
 * @Description: 微信支付-结算信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPaySettleInfoDTO implements Serializable {

    @ApiModelProperty("是否指定分账")
    private boolean profit_sharing;
}
