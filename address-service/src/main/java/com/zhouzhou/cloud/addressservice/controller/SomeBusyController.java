package com.zhouzhou.cloud.addressservice.controller;

import com.zhouzhou.cloud.addressservice.service.SomeBusyService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/someBusyThing")
public class SomeBusyController {

    @Resource
    private SomeBusyService someBusyService;

    @PostMapping("/doSomeBusyWork")
    public void doSomeBusyWork(@RequestParam("name") String name) throws InterruptedException {
        someBusyService.doSomeBusyWork(name);
    }
}
