package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "用户账号 ID")
    private Long userId;

    @Schema(name = "用户账号")
    private String userName;

    @Schema(name = "用户编码")
    private String userCode;

    @Schema(name = "用户所在租户唯一Code")
    private String tenantCode;
}
