package com.zhouzhou.cloud.common.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BaseLimitReq extends BaseAMO {
    @Schema(name = "获取条数")
    protected Integer limit;

}
