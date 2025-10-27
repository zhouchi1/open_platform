package com.zhouzhou.cloud.common.enums.messageservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-06-02
 * @Description: 消息接收类型枚举
 */
@Getter
@AllArgsConstructor
public enum MessageReceiveEnum {

    READ("READ", "已读"),

    UNREAD("UNREAD", "未读");

    /**
     * key
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;

    public static MessageReceiveEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (MessageReceiveEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
