package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "是否指定分账")
    private boolean profit_sharing;
}
