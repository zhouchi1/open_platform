package com.zhouzhou.cloud.userservice.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaasPlatformProcessReq implements Serializable {

    private static final long serialVersionUID = 7812634871264871628L;

    @NotNull(message = "平台唯一标识不能为空")
    @Schema(name = "openPlatform平台分配唯一ID标识")
    private String appId;

    @NotNull(message = "平台分配公钥不能为空")
    @Schema(name = "openPlatform平台分配公钥")
    private String appPublicSecret;

    @NotNull(message = "处理类型不能为空")
    @Schema(name = "处理类型（1：开启、2：关闭、3：删除）")
    private Integer processType;
}
