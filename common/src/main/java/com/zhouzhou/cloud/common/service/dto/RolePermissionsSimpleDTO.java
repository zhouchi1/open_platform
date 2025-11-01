package com.zhouzhou.cloud.common.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "菜单ID")
    private Integer menuId;

    @Schema(name = "菜单类型 menu:菜单 page:页面 button:按钮")
    private String type;

    @Schema(name = "菜单名称")
    private String menuName;

    @Schema(name = "菜单路径")
    private String path;

    @Schema(name = "icon")
    private String icon;

    @Schema(name = "排序值")
    private Integer sort;

    /**
     * 上级菜单ID
     */
    @Schema(name = "上级菜单ID")
    private Integer parentId;

    /**
     * 是否选中
     */
    @Schema(name = "是否选中")
    private Boolean selected;

}
