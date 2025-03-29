package com.zhouzhou.cloud.websocketservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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

    @NotNull(message = "消息内容不能为空")
    @ApiModelProperty(value = "消息内容",required = true)
    private String message;

    @NotNull(message = "发送消息方用户Id不能为空")
    @ApiModelProperty(value = "发送消息方通道Id",required = true)
    private String sendMessageChannelId;

    @NotNull(message = "接收消息方用户Id不能为空")
    @ApiModelProperty(value = "接收消息方通道Id",required = true)
    private String acceptMessageChannelId;

    @NotNull(message = "消息是否广播发送到所有人不能为空")
    @ApiModelProperty(value = "消息是否广播发送到所有人",required = true)
    private boolean broadcast;
}
