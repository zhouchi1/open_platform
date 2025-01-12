package com.zhouzhou.cloud.common.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class BaseNameReq extends BaseAMO {

    private static final long serialVersionUID = 6233681715790483695L;

    @ApiModelProperty("名称")
    private String name;


}
