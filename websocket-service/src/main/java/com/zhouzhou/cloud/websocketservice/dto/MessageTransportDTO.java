package com.zhouzhou.cloud.websocketservice.dto;

import com.zhouzhou.cloud.websocketservice.enums.MessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
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

    private static final Long serialVersionUID = 6475839755638363489L;

    @ApiModelProperty("消息内容")
    private String message;

    @ApiModelProperty("消息是否广播发送到所有人")
    private boolean broadcast;

    @ApiModelProperty("消息Id")
    private String messageId;

    @ApiModelProperty("消息类型")
    private MessageTypeEnum messageType;

    @ApiModelProperty("发送消息方用户信息")
    private MessageSendUserInfoDTO messageSendUserInfoDTO;

    @ApiModelProperty("接收消息方用户信息")
    private MessageAcceptUserInfoDTO messageAcceptUserInfoDTO;
}
