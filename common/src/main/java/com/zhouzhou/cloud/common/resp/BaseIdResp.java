package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class BaseIdResp extends BaseAMO {

    private static final long serialVersionUID = 6384626487326487326L;

    @ApiModelProperty("id")
    private Long id;
}
