package com.zhouzhou.cloud.payservice.response.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResult {

    private boolean success;

    private String message;

    private String transactionId;

    private String errorCode;

    public PaymentResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public PaymentResult(boolean success, String message, String transactionId) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
    }

    public static PaymentResult success(String transactionId) {
        return new PaymentResult(true, "支付成功", transactionId);
    }

    public static PaymentResult failure(String message) {
        return new PaymentResult(false, message);
    }
}
