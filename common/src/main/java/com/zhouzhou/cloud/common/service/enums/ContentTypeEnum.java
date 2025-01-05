package com.zhouzhou.cloud.common.service.enums;

/**
 * 审批内容类型-枚举
 * @author sunqingrui
 */
public enum ContentTypeEnum {
    /**
     * 单行文本框
     */
    SINGLE_LINE_TEXT_BOX_TYPE(0),
    /**
     * 明细/表格
     */
    FORM_DETATILS_TYPE(1),
    /**
     * 图片
     */
    IMAGE_TYPE(2);

    private final Integer code;

    ContentTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
