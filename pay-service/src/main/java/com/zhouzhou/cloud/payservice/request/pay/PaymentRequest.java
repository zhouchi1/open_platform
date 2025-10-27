package com.zhouzhou.cloud.payservice.request.pay;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest{

    @Schema(name = "订单Id")
    private String orderId;

    private String paymentType;

    @Schema(name = "选中的银行卡ID")
    private String cardId;

    @Schema(name = "选中的优惠活动ID")
    private String promotionId;

    private BigDecimal amount;

    @Schema(name = "选中的银行卡ID")
    private String payPassword;
}
