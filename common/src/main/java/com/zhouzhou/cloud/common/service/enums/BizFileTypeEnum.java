package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @description: 出入库记录枚举
 * @author: sunqingrui
 */
@Getter
@AllArgsConstructor
public enum BizFileTypeEnum {
    /**
     * 资产图片
     */
    ASSET_PICTURE(0, "资产图片"),
    /**
     * 供应商文件
     */
    SUPPLIER_FILE(1, "供应商文件"),


    /**
     * 统配保函
     */
    INTEGRATION_GUARANTEE(2,"统配保函"),

    /**
     * 中转保函
     */
    TRANSIT_GUARANTEE(3,"中转保函"),
    ;

    /**
     * key
     */
    private final Integer code;
    /**
     * desc
     */
    private final String desc;

    public static BizFileTypeEnum matchCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (BizFileTypeEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
