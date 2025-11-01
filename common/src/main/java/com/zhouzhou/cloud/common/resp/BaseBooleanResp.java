package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)

@Data
@Builder
public class BaseBooleanResp extends BaseAMO {

    @Schema(name = "是否")
    private Boolean aBoolean;
}
