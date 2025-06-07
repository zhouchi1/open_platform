package com.zhouzhou.cloud.common.enums.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-06-02
 * @Description: 消息发送类型枚举
 */
@Getter
@AllArgsConstructor
public enum MessageSendEnum {

    BROADCAST("BROADCAST", "广播聊"),

    SINGLE("SINGLE", "单聊"),

    GROUP("GROUP", "群组聊");

    /**
     * key
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;

    public static MessageSendEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (MessageSendEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
