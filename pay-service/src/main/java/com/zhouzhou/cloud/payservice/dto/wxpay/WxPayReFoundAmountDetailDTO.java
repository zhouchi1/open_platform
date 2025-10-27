package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-04
 * @Description: 微信支付退款金额详情信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundAmountDetailDTO implements Serializable {

    private static final Long serialVersionUID = 2346237864782364781L;

    @Schema(name = "出资账户类型")
    private String account;

    @Schema(name = "出资金额")
    private Integer amount;
}
