package com.zhouzhou.cloud.common.req;

import com.google.common.collect.Lists;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;



@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseIdsReq extends BaseAMO {

    private static final long serialVersionUID = 3957002589868245914L;


    @ApiModelProperty("列表")
    @NotNull(message = "列表缺少内容ID")
    @Size(min = 1, message = "列表缺少内容ID")
    private List<Long> ids = Lists.newArrayList();
}
