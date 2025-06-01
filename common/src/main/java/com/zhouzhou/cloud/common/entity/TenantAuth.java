package com.zhouzhou.cloud.common.entity;

import com.zhouzhou.cloud.common.service.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-16
 * @Description: 租户表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TenantAuth extends BaseEntity {

    private static final Long serialVersionUID = 7234562763154761523L;

    @ApiModelProperty("saas平台外部唯一识别ID")
    private String appId;

    @ApiModelProperty("saas平台识别公共密钥")
    private String appPublicSecret;

    @ApiModelProperty("第三方saas账号是否生效")
    private Boolean status;
}
