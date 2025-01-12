package com.zhouzhou.cloud.common.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@ApiModel
@Getter
@Setter
public class BasePageReq extends BaseAMO {

    private static final long serialVersionUID = 2500042839985050712L;

    @ApiModelProperty(value = "当前页码", required = true)
    @NotNull(message = "当前页码参数不能为null")
    @Min(value = 0, message = "当前页码非法参数")
    protected long pageNo;

    @ApiModelProperty(value = "当前页数量", required = true)
    @NotNull(message = "当前页数量不能为null")
    @Min(value = 0, message = "当前页数量参数错误")
    @Max(value = 1000, message = "当前页数量最大为1000")
    protected long pageSize;

}
