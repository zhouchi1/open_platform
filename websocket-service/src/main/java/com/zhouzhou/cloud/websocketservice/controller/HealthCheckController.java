package com.zhouzhou.cloud.websocketservice.controller;

import com.zhouzhou.cloud.common.service.enums.ResponseStateEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 *
 * @author 张鑫彬
 * @date 2025-07-10
 */
@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @RequestMapping("/check")
    public String healthCheck() {
        return ResponseStateEnum.SUCCESS.getDisplay();
    }

}