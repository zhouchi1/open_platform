package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class BasePriceResp extends BaseAMO {

    private static final long serialVersionUID = 6872634825815419845L;

    @Schema(name = "price")
    private BigDecimal price;
}
