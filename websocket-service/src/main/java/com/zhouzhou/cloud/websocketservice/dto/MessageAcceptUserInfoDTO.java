package com.zhouzhou.cloud.websocketservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageAcceptUserInfoDTO implements Serializable {

    private static final long serialVersionUID = -1273816033405430288L;

    @Schema(name = "用户id")
    private List<String> userIds;

    @Schema(name = "广播接收用户id")
    private String currentUserId;
}
