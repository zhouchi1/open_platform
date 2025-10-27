package com.zhouzhou.cloud.websocketservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageSendUserInfoDTO implements Serializable {

    @Schema(name = "用户id")
    private String userId;
}
