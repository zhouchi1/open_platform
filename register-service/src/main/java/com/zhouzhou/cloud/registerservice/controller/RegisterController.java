package com.zhouzhou.cloud.registerservice.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "微服务注册控制器")
@RestController
@RequestMapping("/register")
public class RegisterController {

    @PostMapping("/people")
    public String hello(){
        return "hello world";
    }
}
