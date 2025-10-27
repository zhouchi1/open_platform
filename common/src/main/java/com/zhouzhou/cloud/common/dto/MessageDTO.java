package com.zhouzhou.cloud.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 7621348761287461892L;

    @Schema(name = "终端消息接收用户Id")
    private String targetUserId;

    @Schema(name = "终端用户saas平台唯一识别标识")
    private String appId;

    @Schema(name = "终端用户接收的消息")
    private String message;

    @Schema(name = "消息类型")
    private String type;

    @Schema(name = "消息Id")
    private String messageId;
}
