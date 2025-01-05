package com.zhouzhou.cloud.common.service.enums;

/**
 * @Description 操作类型枚举
 * @Author sqr
 */
public enum OperationTypeEnum {
    /**
     * 0-新增
     */
    ADD(0, "新增"),
    /**
     * 1-删除
     */
    DEL(1, "删除"),
    /**
     * 2-修改
     */
    EDIT(2, "修改"),
    /**
     * 3-查询
     */
    QUERY(3, "查询"),
    /**
     * 4-统计
     */
    STAT(4, "统计"),
    /**
     * 5-导出
     */
    EXPORT(5, "导出"),
    /**
     * 6-导入
     */
    IMPORT(6, "导入"),
    /**
     * 7-审核
     */
    AUTH(7, "审核"),
    /**
     * 8-切换
     */
    SWITCH(8, "切换"),
    /**
     * 9-保存
     */
    SAVE(9, "保存");

    /**
     * 枚举值
     */
    final int value;
    /**
     * 枚举名称
     */
    final String display;

    OperationTypeEnum(int value, String display) {
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
