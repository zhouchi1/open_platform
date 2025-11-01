package com.zhouzhou.cloud.common.service.dto;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sqr
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRoleSimpleDTO extends BaseAMO {

    @Schema(name = "角色ID")
    private Integer roleId;

    @Schema(name = "角色名")
    private String roleName;

}
