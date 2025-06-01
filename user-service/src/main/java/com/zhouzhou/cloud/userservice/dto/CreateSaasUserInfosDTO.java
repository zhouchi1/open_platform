package com.zhouzhou.cloud.userservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSaasUserInfosDTO implements Serializable {

    private static final long serialVersionUID = 2164782364872364786L;

    @ApiModelProperty("saas平台用户识别信息")
    private String userName;
}
