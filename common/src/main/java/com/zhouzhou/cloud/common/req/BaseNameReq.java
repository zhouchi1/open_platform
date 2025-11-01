package com.zhouzhou.cloud.common.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseNameReq extends BaseAMO {

    private static final long serialVersionUID = 6233681715790483695L;

    @Schema(name = "名称")
    private String name;


}
