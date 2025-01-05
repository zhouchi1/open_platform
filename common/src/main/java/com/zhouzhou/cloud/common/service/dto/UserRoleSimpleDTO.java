package com.zhouzhou.cloud.common.service.dto;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sqr
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class UserRoleSimpleDTO extends BaseAMO {

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    @ApiModelProperty(value = "角色名")
    private String roleName;

}
