package com.zhouzhou.cloud.websocketservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    MESSAGE_CONFIRM("MESSAGE_CONFIRM", "消息接收确认"),

    SEND_MESSAGE("SEND_MESSAGE", "发送消息"),
    ;

    private final String code;

    private final String desc;

    public static MessageTypeEnum matchCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (MessageTypeEnum value : values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }
}
