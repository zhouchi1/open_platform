package com.zhouzhou.cloud.userservice.annotation;

import java.lang.annotation.*;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignSaasIdentity {

}
