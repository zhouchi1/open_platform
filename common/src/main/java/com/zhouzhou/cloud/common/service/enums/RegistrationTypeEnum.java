package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入驻类型枚举
 *
 * @author 张鑫彬
 * @date 2024-12-18 14:51:35
 */
@Getter
@AllArgsConstructor
public enum RegistrationTypeEnum {

    /**
     * 零售门店
     */
    RETAIL_STORE("RETAIL_STORE", "零售门店"),
    /**
     * 授权经销商
     */
    AUTHORIZED_DEALER("AUTHORIZED_DEALER", "授权经销商"),
    ;


    /**
     * 编码
     */
    private final String code;

    /**
     * 名称
     */
    private final String name;


}
