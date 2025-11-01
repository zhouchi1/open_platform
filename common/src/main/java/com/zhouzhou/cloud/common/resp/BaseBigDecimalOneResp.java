package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;



@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class BaseBigDecimalOneResp extends BaseAMO {

    @Schema(name = "合计")
    private BigDecimal bigDecimal;
}
