package com.zhouzhou.cloud.websocketservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityCheckCompleteDTO implements Serializable {

    private static final long serialVersionUID = 3658732374926453287L;

    @ApiModelProperty("通道Id")
    private String channelId;

    @ApiModelProperty("平台用户唯一识别信息")
    private String userPlatformUniqueInfo;

    @ApiModelProperty("连接token")
    private String connectToken;

    @ApiModelProperty("连接时间")
    private LocalDateTime connectTime;
}
