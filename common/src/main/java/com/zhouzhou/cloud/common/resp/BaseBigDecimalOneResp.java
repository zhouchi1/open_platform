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
public class BaseBigDecimalOneResp extends BaseAMO {

    private static final long serialVersionUID = 7534725532452244234L;

    @ApiModelProperty("合计")
    private BigDecimal bigDecimal;
}
