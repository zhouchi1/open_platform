package com.zhouzhou.cloud.common.annotation;

import java.lang.annotation.*;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-19
 * @Description: 程序中断数据处理自定义注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InterruptRestore {

    String group();

    int step();

    String name();
}
