package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 级别枚举
 * @author: ryf
 * @Date: 2024/1/3 15:05
 */
@Getter
@AllArgsConstructor
public enum LevelEnum {
    /**
     * 一级
     */
    FIRST_LEVEL("FIRST_LEVEL", "一级", 1),
    /**
     * 二级
     */
    SECOND_LEVEL("SECOND_LEVEL", "二级", 2),
    /**
     * 三级
     */
    THIRD_LEVEL("THIRD_LEVEL", "三级", 3),

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

    public static LevelEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (LevelEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }

    public static LevelEnum matchTag(Integer tag) {
        if (Objects.isNull(tag)) {
            return null;
        }
        for (LevelEnum value : values()) {
            if (Objects.equals(value.tag, tag)) {
                return value;
            }
        }
        return null;
    }
}
