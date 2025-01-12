package com.zhouzhou.cloud.common.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@ApiModel
@Getter
@Setter
public class BaseLimitReq extends BaseAMO {
    @ApiModelProperty(value = "获取条数")
    protected Integer limit;

}
