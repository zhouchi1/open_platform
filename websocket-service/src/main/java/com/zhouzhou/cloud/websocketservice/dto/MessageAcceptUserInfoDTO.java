package com.zhouzhou.cloud.websocketservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageAcceptUserInfoDTO implements Serializable {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户通道id")
    private String channelId;
}
