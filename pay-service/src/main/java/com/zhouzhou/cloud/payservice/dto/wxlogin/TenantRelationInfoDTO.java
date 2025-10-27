package com.zhouzhou.cloud.payservice.dto.wxlogin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 登录租户信息返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantRelationInfoDTO implements Serializable {

    private static final long serialVersionUID = 8723647235476908772L;

    @Schema(name = "租户编码")
    private String tenantCode;

    @Schema(name = "租户名称")
    private String tenantName;
}
