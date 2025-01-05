package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 商品保质期类型
 * @author: ryf
 * @Date: 2023/12/21 16:40
 */
@Getter
@AllArgsConstructor
public enum ExpireTypeEnum {

    /**
     * 永久有效
     */
    VALID_FOREVER("VALID_FOREVER", "永久有效", 0),
    /**
     * 小时
     */
    HOUR("HOUR", "小时", 1),
    /**
     * 天
     */
    DAY("DAY", "天", 2),
    /**
     * 月
     */
    MONTH("MONTH", "月", 3),
    /**
     * 年
     */
    YEAR("YEAR", "年", 4),


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

    public static ExpireTypeEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (ExpireTypeEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }

    public static ExpireTypeEnum matchTag(Integer tag) {
        if (Objects.isNull(tag)) {
            return null;
        }
        for (ExpireTypeEnum value : values()) {
            if (Objects.equals(value.tag, tag)) {
                return value;
            }
        }
        return null;
    }
}
