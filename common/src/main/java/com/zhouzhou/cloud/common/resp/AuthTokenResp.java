package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@Builder
public class AuthTokenResp extends BaseAMO {

    @ApiModelProperty("访问接口 TOKEN")
    private String accessToken;

    @ApiModelProperty("刷新访问接口TOKEN 刷新TOKEN")
    private String refreshToken;

    @ApiModelProperty("用户信息")
    private UserResp userResp;

}


