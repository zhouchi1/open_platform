package com.zhouzhou.cloud.productservice.controller;

import com.zhouzhou.cloud.common.service.base.ResponseData;
import com.zhouzhou.cloud.common.utils.ResponseDataUtil;
import com.zhouzhou.cloud.productservice.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ProductCartesianController {

    @Resource
    private ProductService productService;

    @PostMapping("/addProductInfoByCartesian")
    ResponseData<?> addProductInfoByCartesian(){
        productService.addProductInfoByCartesian();
        return ResponseDataUtil.success();
    }
}
