package com.zhouzhou.cloud.common.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseIdReq extends BaseAMO {
    private static final long serialVersionUID = 3957002589868245914L;
    @ApiModelProperty("主键ID")
    @NotNull(message = "主键ID为空")
    private Long id;
}
