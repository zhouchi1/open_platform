package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@Builder
public class BaseBooleanResp extends BaseAMO {
    @ApiModelProperty("是否")
    private Boolean aBoolean;


}
