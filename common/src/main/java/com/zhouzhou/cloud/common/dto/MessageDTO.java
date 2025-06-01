package com.zhouzhou.cloud.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 7621348761287461892L;

    @ApiModelProperty("终端消息接收用户Id")
    private String targetUserId;

    @ApiModelProperty("终端用户saas平台唯一识别标识")
    private String appId;

    @ApiModelProperty("终端用户接收的消息")
    private String message;

    @ApiModelProperty("消息类型")
    private String type;

    @ApiModelProperty("消息Id")
    private String messageId;
}
