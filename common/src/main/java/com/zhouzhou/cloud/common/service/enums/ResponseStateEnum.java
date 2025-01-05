package com.zhouzhou.cloud.common.service.enums;

/**
 * @Description 响应状态枚举
 * @Author sqr
 **/
public enum ResponseStateEnum {
    /**
     * 0-失败
     */
    FAIL(0, "失败"),
    /**
     * 1-成功
     */
    SUCCESS(1, "成功"),
    ;
    /**
     * 枚举值
     */
    final int value;
    /**
     * 枚举名称
     */
    final String display;

    ResponseStateEnum(int value, String display) {
        this.value = value;
        this.display = display;
    }

    public static OperationTypeEnum valueOf(int value) {
        for (OperationTypeEnum c : OperationTypeEnum.values()) {
            if (c.getValue() == value) {
                return c;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }
}
