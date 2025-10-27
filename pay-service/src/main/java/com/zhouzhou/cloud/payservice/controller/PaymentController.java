package com.zhouzhou.cloud.payservice.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zhouzhou.cloud.payservice.dto.common.PaymentOptionDTO;
import com.zhouzhou.cloud.payservice.request.pay.PaymentRequest;
import com.zhouzhou.cloud.payservice.response.wxpay.PaymentResult;
import com.zhouzhou.cloud.payservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "商城小程序 >> 支付接口")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Operation(description = "获取支付选项列表")
    @ApiOperationSupport(author = "Sr.Zhou",order = 1)
    @PostMapping("/options")
    public ResponseEntity<List<PaymentOptionDTO>> getPaymentOptions(@RequestParam String orderId) {
        try {
            List<PaymentOptionDTO> options = paymentService.getAvailablePaymentOptions(orderId);
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "执行支付")
    @ApiOperationSupport(author = "Sr.Zhou",order = 2)
    @PostMapping("/execute")
    public ResponseEntity<PaymentResult> executePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentResult result = paymentService.executePayment(paymentRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            PaymentResult errorResult = PaymentResult.failure("支付处理异常: " + e.getMessage());
            return ResponseEntity.ok(errorResult);
        }
    }
}
