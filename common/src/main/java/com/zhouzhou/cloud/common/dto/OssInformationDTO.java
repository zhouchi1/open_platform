package com.zhouzhou.cloud.common.dto;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: 周驰
 * @Date: 2024/06/20/9:29
 * @Description: OSS临时授权信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OssInformationDTO extends BaseAMO {

    private static final long serialVersionUID = 5671254671257612948L;

    @ApiModelProperty("临时授权Id")
    private String accessKeyId;

    @ApiModelProperty("临时身份密钥")
    private String accessKeySecret;

    @ApiModelProperty("到期时间")
    private String expiration;

    @ApiModelProperty("Sts token")
    private String securityToken;
}
