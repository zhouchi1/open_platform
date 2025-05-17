package com.zhouzhou.cloud.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdentityInfoDTO implements Serializable {

    private static final long serialVersionUID = 3278462378648723641L;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("用户saas平台类型")
    private String userSaasPlatformType;
}
