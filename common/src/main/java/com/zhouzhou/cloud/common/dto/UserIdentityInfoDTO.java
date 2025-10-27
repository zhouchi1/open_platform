package com.zhouzhou.cloud.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdentityInfoDTO implements Serializable {

    private static final long serialVersionUID = 3278462378648723641L;

    @Schema(name = "用户Id")
    private String userId;

    @Schema(name = "用户saas平台唯一识别信息")
    private String appId;

    @Schema(name = "用户Id")
    private String userCode;
}
