package com.zhouzhou.cloud.payservice.response.wxlogin;

import com.zhouzhou.cloud.payservice.dto.wxlogin.TenantRelationInfoDTO;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 微信小程序登录响应实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WxLoginResp extends BaseAMO {

    private static final long serialVersionUID = 7846238746283764873L;

    @Schema(name = "登录授权Token")
    private String token;

    @Schema(name = "最后一次登录使用的商户编码(供前端参考)")
    private String tenantCode;

    @Schema(name = "登录人的商户信息")
    private List<TenantRelationInfoDTO> tenantRelationInfoDTOList;
}
