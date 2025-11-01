package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class BaseUrlResp extends BaseAMO {


    private static final long serialVersionUID = -6788312595539982331L;


    @Schema(name = "地址")
    protected List<String> urls;

}
