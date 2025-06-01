package com.zhouzhou.cloud.userservice.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaasUserCreateReq implements Serializable {

    private static final long serialVersionUID = 1273818263871263723L;

    @ApiModelProperty("saas平台方唯一识别标识")
    private String saasPlatformAppId;

    @ApiModelProperty("saas平台方密钥")
    private String saasPlatformSecret;

    @ApiModelProperty("saas平台方用户唯一标识")
    private String saasUserId;

    @ApiModelProperty("openPlatform平台用户唯一标识")
    private String openPlatformUserId;
}
