package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@Builder
public class BasePriceResp extends BaseAMO {

    private static final long serialVersionUID = 6872634825815419845L;

    @ApiModelProperty("price")
    private BigDecimal price;
}
