package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "用户账号 ID")
    private Long userId;

    @Schema(name = "用户账号")
    private String userName;

    @Schema(name = "用户编码")
    private String userCode;

    @Schema(name = "是否是管理员")
    private Boolean admin;
}
