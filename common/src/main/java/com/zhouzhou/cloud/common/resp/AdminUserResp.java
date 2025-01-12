package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-21
 * @Description: 管理平台登录用户信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResp extends BaseAMO {

    @ApiModelProperty("用户账号 ID")
    private Long userId;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("用户编码")
    private String userCode;

    @ApiModelProperty("是否是管理员")
    private Boolean admin;
}
