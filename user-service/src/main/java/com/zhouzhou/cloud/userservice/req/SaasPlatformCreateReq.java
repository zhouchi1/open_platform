package com.zhouzhou.cloud.userservice.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SaasPlatformCreateReq extends BaseAMO {

    private static final long serialVersionUID = 6328764872364876238L;

    @NotNull(message = "saas平台唯一识别平台类型不能为空")
    @ApiModelProperty("saas平台唯一识别平台类型")
    private String saasPlatformType;

    @NotNull(message = "saas平台公钥不能为空")
    @ApiModelProperty("saas平台自身公钥")
    private String appPublicSecret;
}
