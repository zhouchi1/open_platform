package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@Builder
public class BaseStringResp extends BaseAMO {


    private static final long serialVersionUID = -6788312595539982331L;

    /**
     * string
     */
    @ApiModelProperty("data")
    private String str;

}
