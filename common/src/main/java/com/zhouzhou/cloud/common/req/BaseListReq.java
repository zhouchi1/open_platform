package com.zhouzhou.cloud.common.req;

import com.google.common.collect.Lists;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@ApiModel
@Getter
@Setter
public class BaseListReq<T> extends BaseAMO {

    private static final long serialVersionUID = -5418162927437609447L;

    @ApiModelProperty("列表")
    @NotNull(message = "列表缺少内容")
    @Size(min = 1, message = "列表缺少内容")
    @Valid
    private List<T> records = Lists.newArrayList();

}
