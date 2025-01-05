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
    private Long userId;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("用户编码")
    private String userCode;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("是否是管理员")
    private Boolean admin;

    @ApiModelProperty("系统图片分组编码")
    private String pictureGroupCode;

    @ApiModelProperty("微信唯一区分ID")
    private String openId;

    @ApiModelProperty("用户所在租户唯一Code")
    private String tenantCode;

}
