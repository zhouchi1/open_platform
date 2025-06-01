package com.zhouzhou.cloud.userservice.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessSaasReq<T> {

    @NotNull(message = "唯一识别码不能为空")
    @ApiModelProperty("saas平台唯一识别码")
    private String appId;

    @NotNull(message = "签名内容不能为空")
    @ApiModelProperty("签名内容")
    private String payload;

    @NotNull(message = "时间戳不能为空")
    @ApiModelProperty("时间戳")
    private String timeStamp;

    @NotNull(message = "随机字符串不能为空")
    @ApiModelProperty("随机字符串")
    private String nonceStr;

    @NotNull(message = "签名结果不能为空")
    @ApiModelProperty("用私钥签名的结果")
    private String signContext;

    @ApiModelProperty("平台侧用于解析签名内容（客户端无需传递此项）")
    private T parsedPayload;
}

