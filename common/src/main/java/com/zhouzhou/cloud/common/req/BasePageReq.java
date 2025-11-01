package com.zhouzhou.cloud.common.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePageReq extends BaseAMO {

    private static final long serialVersionUID = 2500042839985050712L;

    @Schema(name =  "当前页码", required = true)
    @NotNull(message = "当前页码参数不能为null")
    @Min(value = 0, message = "当前页码非法参数")
    protected long pageNo;

    @Schema(name =  "当前页数量", required = true)
    @NotNull(message = "当前页数量不能为null")
    @Min(value = 0, message = "当前页数量参数错误")
    @Max(value = 1000, message = "当前页数量最大为1000")
    protected long pageSize;

}
