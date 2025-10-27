package com.zhouzhou.cloud.websocketservice.dto;

import com.zhouzhou.cloud.common.enums.messageservice.MessageKindEnum;
import com.zhouzhou.cloud.websocketservice.enums.MessageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 消息传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageTransportDTO implements Serializable {

    private static final long serialVersionUID = 6475839755638363489L;

    @Schema(name = "消息内容")
    private String message;

    @Schema(name = "消息是否广播发送到所有人")
    private boolean broadcast;

    @Schema(name = "消息Id")
    private String messageId;

    @Schema(name = "消息类型")
    private MessageTypeEnum messageType;

    @Schema(name = "发送消息方用户信息")
    private MessageSendUserInfoDTO messageSendUserInfoDTO;

    @Schema(name = "接收消息方用户信息")
    private MessageAcceptUserInfoDTO messageAcceptUserInfoDTO;

    @Schema(name = "消息类别")
    private MessageKindEnum messageKind;

    @Schema(name = "数据id")
    private Long dataId;
}
