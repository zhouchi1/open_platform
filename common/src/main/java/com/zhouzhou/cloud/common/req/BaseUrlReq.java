package com.zhouzhou.cloud.common.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseUrlReq extends BaseAMO {
    private static final long serialVersionUID = -7298280599764406272L;
    @Schema(name = "url地址")
    private String url;

    @Schema(name = "文件名")
    private String name;

    @Schema(name = "文件大小")
    private String size;

    @Schema(name = "备注")
    private String remark;

}
