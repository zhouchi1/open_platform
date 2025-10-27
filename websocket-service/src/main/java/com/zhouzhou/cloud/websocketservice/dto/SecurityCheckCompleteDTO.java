package com.zhouzhou.cloud.websocketservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "通道Id")
    private String channelId;

    @Schema(name = "用户Id")
    private String userId;

    @Schema(name = "连接token")
    private String connectToken;

    @Schema(name = "连接时间")
    private LocalDateTime connectTime;

    @Schema(name = "用户编码")
    private String userCode;
}
