package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 一品多码 类型
 * @author: ryf
 * @Date: 2024/1/3 14:45
 */
@Getter
@AllArgsConstructor
public enum ProductUpcTypeEnum {
    /**
     * 普通条码
     */
    NORMAL_BAR_CODE("NORMAL_BAR_CODE", "普通条码", 0),
    /**
     * 箱码
     */
    BOX_CODE("BOX_CODE", "箱码", 1),

    ;

    /**
     * key
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;
    /**
     * tag
     */
    private final Integer tag;

    public static ProductUpcTypeEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (ProductUpcTypeEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }

    public static ProductUpcTypeEnum matchTag(Integer tag) {
        if (Objects.isNull(tag)) {
            return null;
        }
        for (ProductUpcTypeEnum value : values()) {
            if (Objects.equals(value.tag, tag)) {
                return value;
            }
        }
        return null;
    }
}
