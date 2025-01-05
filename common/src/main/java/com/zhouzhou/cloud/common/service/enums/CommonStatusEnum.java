package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * describe:通用状态
 *
 * @author wangyan
 * @date 2021/12/16
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {
    /**
     * 正常
     */
    NORMAL("正常"),
    /**
     * 暂停
     */
    SUSPEND("暂停"),
    /**
     * 新增
     */
    ADD("新增"),
    /**
     * 修改
     */
    UPDATE("修改"),
    /**
     * 禁售
     */
    FORBID("禁售"),
    /**
     * 删除
     */
    DELETE("删除");
    /**
     * 详细信息
     */
    private final String msg;
}
