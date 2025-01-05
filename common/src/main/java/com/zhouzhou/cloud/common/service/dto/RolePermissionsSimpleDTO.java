package com.zhouzhou.cloud.common.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author sqr
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolePermissionsSimpleDTO extends BaseAMO {

    @ApiModelProperty(value = "菜单ID")
    private Integer menuId;

    @ApiModelProperty("菜单类型 menu:菜单 page:页面 button:按钮")
    private String type;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "菜单路径")
    private String path;

    @ApiModelProperty("icon")
    private String icon;

    @ApiModelProperty("排序值")
    private Integer sort;

    /**
     * 上级菜单ID
     */
    @ApiModelProperty("上级菜单ID")
    private Integer parentId;

    /**
     * 是否选中
     */
    @ApiModelProperty("是否选中")
    private Boolean selected;

}
