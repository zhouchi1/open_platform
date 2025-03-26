package com.zhouzhou.cloud.websocketservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageTransportDTO implements Serializable {

    private static final Long serialVersionUID = 6475839755638363489L;

    @ApiModelProperty("消息内容")
    private String message;

    @ApiModelProperty("接收消息方用户Id")
    private String acceptMessageUserId;

    @ApiModelProperty("消息是否广播发送到所有人")
    private boolean broadcast;
}
