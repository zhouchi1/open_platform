package com.zhouzhou.cloud.productservice.controller;

import com.zhouzhou.cloud.common.service.base.ResponseData;
import com.zhouzhou.cloud.common.utils.ResponseDataUtil;
import com.zhouzhou.cloud.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
public class ProductCartesianController {

    @Resource
    private ProductService productService;

    @PostMapping("/addProductInfoByCartesian")
    ResponseData<?> addProductInfoByCartesian() {
        productService.addProductInfoByCartesian();
        return ResponseDataUtil.success();
    }

    @PostMapping("/testDeepSeekAi")
    ResponseData<?> testDeepSeekAi(@RequestParam("message") String message) {
        productService.testDeepSeekAi(message);
        return ResponseDataUtil.success();
    }
}
