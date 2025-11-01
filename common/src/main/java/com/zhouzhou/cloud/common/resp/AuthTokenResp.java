package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class AuthTokenResp extends BaseAMO {

    @Schema(name = "访问接口 TOKEN")
    private String accessToken;

    @Schema(name = "刷新访问接口TOKEN 刷新TOKEN")
    private String refreshToken;

    @Schema(name = "用户信息")
    private UserResp userResp;

}


