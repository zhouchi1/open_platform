package com.zhouzhou.cloud.common.service.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-21
 * @Description: 微信小程序登录用户信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemUserResp extends BaseAMO {

    @ApiModelProperty("用户账号 ID")
    private String userId;

    @ApiModelProperty("saas平台内部类型标识")
    private String saasPlatformType;
}
