package com.zhouzhou.cloud.common.req;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseIdReq extends BaseAMO {

    private static final long serialVersionUID = 3957002589868245914L;

    @Schema(name = "主键ID")
    @NotNull(message = "主键ID为空")
    private Long id;
}
