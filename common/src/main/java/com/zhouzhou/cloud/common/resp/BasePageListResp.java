package com.zhouzhou.cloud.common.resp;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasePageListResp<T> extends BaseAMO {

    private static final long serialVersionUID = -649179224764476897L;

    @Schema(name = "列表")
    private List<T> records = Lists.newArrayList();

    @Schema(name = "总数")
    private long pageTotal;

    @Schema(name = "条数")
    private long pageSize;

    @Schema(name = "页码")
    private long pageNo;

}
