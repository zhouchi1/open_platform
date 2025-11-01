package com.zhouzhou.cloud.userservice.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessSaasReq<T> {

    @NotNull(message = "唯一识别码不能为空")
    @Schema(name = "saas平台唯一识别码")
    private String appId;

    @NotNull(message = "签名内容不能为空")
    @Schema(name = "签名内容")
    private String payload;

    @NotNull(message = "时间戳不能为空")
    @Schema(name = "时间戳")
    private String timeStamp;

    @NotNull(message = "随机字符串不能为空")
    @Schema(name = "随机字符串")
    private String nonceStr;

    @NotNull(message = "签名结果不能为空")
    @Schema(name = "用私钥签名的结果")
    private String signContext;

    @Schema(name = "平台侧用于解析签名内容（客户端无需传递此项）")
    private T parsedPayload;
}

