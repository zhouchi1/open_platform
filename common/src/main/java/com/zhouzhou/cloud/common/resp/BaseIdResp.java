package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class BaseIdResp extends BaseAMO {

    private static final long serialVersionUID = 6384626487326487326L;

    @Schema(name = "id")
    private Long id;
}
