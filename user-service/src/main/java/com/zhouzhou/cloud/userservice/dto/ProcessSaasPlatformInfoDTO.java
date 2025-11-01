package com.zhouzhou.cloud.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessSaasPlatformInfoDTO implements Serializable {

    @NotNull(message = "处理类型不能为空")
    @Schema(name = "Saas平台用户信息处理类型（2：删除、3：启用、4：暂停）")
    private Integer processType;
}
