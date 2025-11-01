package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class BaseStringResp extends BaseAMO {


    private static final long serialVersionUID = -6788312595539982331L;

    /**
     * string
     */
    @Schema(name = "data")
    private String str;

}
