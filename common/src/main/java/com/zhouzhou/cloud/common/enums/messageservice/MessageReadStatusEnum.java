package com.zhouzhou.cloud.common.enums.messageservice;

import com.zhouzhou.cloud.common.enums.EnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageReadStatusEnum implements EnumInterface {

    /**
     * 未读
     */
    @Schema(name = "UNREAD-未读")
    UNREAD("UNREAD", "未读"),
    /**
     * 已读
     */
    @Schema(name = "READ-已读")
    READ("READ", "已读"),
    /**
     * 全部
     */
    @Schema(name = "ALL-全部")
    ALL("ALL", "全部"),
    ;
    private final String code;

    private final String desc;

}
