package com.zhouzhou.cloud.userservice.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaasUserCreateReq implements Serializable {

    private static final long serialVersionUID = 1273818263871263723L;

    @Schema(name = "saas平台方唯一识别标识")
    private String saasPlatformAppId;

    @Schema(name = "saas平台方密钥")
    private String saasPlatformSecret;

    @Schema(name = "saas平台方用户唯一标识")
    private String saasUserId;

    @Schema(name = "openPlatform平台用户唯一标识")
    private String openPlatformUserId;
}
