package com.zhouzhou.cloud.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSaasUserInfosDTO implements Serializable {

    private static final long serialVersionUID = 2164782364872364786L;

    @Schema(name = "saas平台用户识别信息")
    private String userName;
}
