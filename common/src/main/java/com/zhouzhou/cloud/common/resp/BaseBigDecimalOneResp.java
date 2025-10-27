package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;



@ApiModel
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class BaseBigDecimalOneResp extends BaseAMO {

    @ApiModelProperty("合计")
    private BigDecimal bigDecimal;
}
