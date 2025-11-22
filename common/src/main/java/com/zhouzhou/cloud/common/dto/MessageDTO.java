package com.zhouzhou.cloud.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7621348761287461892L;

    @Schema(name = "发送方用户id")
    private String sendUserId;

    @Schema(name = "终端消息接收用户Id")
    private List<String> targetUserIds;

    @Schema(name = "如果要发送的用户不在本台服务器上 则进行Redis广播 将消息发送到每个节点上")
    private String notOnThisNodeUserId;

    @Schema(name = "终端用户接收的消息")
    private String message;

    @Schema(name = "消息类型")
    private String type;

    @Schema(name = "消息Id")
    private String messageId;

    @Schema(name = "消息是否进行广播")
    private boolean broadcast;
}
