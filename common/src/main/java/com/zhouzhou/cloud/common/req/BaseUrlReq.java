package com.zhouzhou.cloud.common.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@ApiModel
@Getter
@Setter
public class BaseUrlReq extends BaseAMO {
    private static final long serialVersionUID = -7298280599764406272L;
    @ApiModelProperty(value = "url地址")
    private String url;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "文件大小")
    private String size;

    @ApiModelProperty(value = "备注")
    private String remark;

}
