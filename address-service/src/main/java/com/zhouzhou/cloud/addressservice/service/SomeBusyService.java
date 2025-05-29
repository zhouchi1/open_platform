package com.zhouzhou.cloud.addressservice.service;

import com.zhouzhou.cloud.common.systemreboot.aspect.annotation.InterruptRestoreAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SomeBusyService {

    @InterruptRestoreAnnotation(key = "doSomeBusyWork")
    public void doSomeBusyWork(String name) throws InterruptedException {

        log.info("method execute:" + name);

        Thread.sleep(10000);
    }
}
