package com.zhouzhou.cloud.common.req;

import com.google.common.collect.Lists;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class BaseIdsReq extends BaseAMO {

    private static final long serialVersionUID = 3957002589868245914L;


    @Schema(name = "列表")
    @NotNull(message = "列表缺少内容ID")
    @Size(min = 1, message = "列表缺少内容ID")
    private List<Long> ids = Lists.newArrayList();
}
