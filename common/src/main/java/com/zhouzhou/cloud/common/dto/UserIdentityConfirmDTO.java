package com.zhouzhou.cloud.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdentityConfirmDTO implements Serializable {

    private static final Long serialVersionUID = 6237846238764872361L;

    @ApiModelProperty("appId")
    private String appId;

    @ApiModelProperty("sign签名")
    private String sign;

    @ApiModelProperty("随机字符串")
    private String nonce;

    @ApiModelProperty("时间戳")
    private Long timestamp;

    @ApiModelProperty("用户Id")
    private String userId;
}
