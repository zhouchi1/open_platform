package com.zhouzhou.cloud.common.resp;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasePageListResp<T> extends BaseAMO {

    private static final long serialVersionUID = -649179224764476897L;

    @ApiModelProperty("列表")
    private List<T> records = Lists.newArrayList();

    @ApiModelProperty("总数")
    private long pageTotal;

    @ApiModelProperty("条数")
    private long pageSize;

    @ApiModelProperty("页码")
    private long pageNo;

}
