package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-04
 * @Description: 微信支付退款金额信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundAmountDTO implements Serializable {

    private static final long serialVersionUID = 7864237846238764824L;

    // --------------------------------必填项目开始---------------------------------------//

    @Schema(name = "退款金额")
    private Integer refund;

    @Schema(name = "原订单金额")
    private Integer total;

    @Schema(name = "退款币种")
    private String currency;

    // --------------------------------必填项目结束---------------------------------------//


    // --------------------------------选填项目开始---------------------------------------//

//    private List<WxPayReFoundAmountDetailDTO> from;

    // --------------------------------选填项目结束---------------------------------------//
}
