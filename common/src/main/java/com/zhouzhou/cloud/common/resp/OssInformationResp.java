package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.dto.OssInformationDTO;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: 周驰
 * @Date: 2024/06/19/11:22
 * @Description: APP上传OSS信息获取
 */
@Data
@Api(tags = "APP上传OSS信息获取")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OssInformationResp extends BaseAMO {

    private static final long serialVersionUID = 8213648732164823247L;

    /**
     * 存储空间
     */
    @ApiModelProperty("存储空间")
    private String bucket;
    /**
     * 地域
     */
    @ApiModelProperty("地域")
    private String region;
    /**
     * 临时授权信息
     */
    @ApiModelProperty("临时授权信息")
    private OssInformationDTO ossInformationDTO;
    /**
     * 根路径
     */
    @ApiModelProperty("根路径")
    private String basePath;
}
