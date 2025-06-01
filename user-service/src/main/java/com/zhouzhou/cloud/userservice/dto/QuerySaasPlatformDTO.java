package com.zhouzhou.cloud.userservice.dto;

import com.zhouzhou.cloud.common.req.BasePageReq;
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
public class QuerySaasPlatformDTO extends BasePageReq {

    private static final long serialVersionUID = 2387125387152376512L;

    @ApiModelProperty("Saas平台授权状态")
    private Boolean saasPlatformStatus;
}
