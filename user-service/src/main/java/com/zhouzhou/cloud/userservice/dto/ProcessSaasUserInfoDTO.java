package com.zhouzhou.cloud.userservice.dto;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessSaasUserInfoDTO implements Serializable {

    @NotNull(message = "处理类型不能为空")
    @ApiModelProperty("Saas平台用户信息处理类型（1：新增、2：删除、3：启用、4：暂停）")
    private Integer processType;

    @NotNull(message = "处理失败时是否跳过处理失败的数据类型不能为空")
    @ApiModelProperty("处理失败时是否跳过处理失败的数据（1：跳过（不处理该条数据）、2：不跳过（全部不处理））")
    private Integer processContinue;

    @NotNull(message = "需要操作的用户集合不能为空")
    @ApiModelProperty("操作Saas用户集合")
    private List<CreateSaasUserInfosDTO> createSaasUserInfosDTOList;
}
