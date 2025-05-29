package com.zhouzhou.cloud.common.systemreboot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-19
 * @Description: 程序中断元数据传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterruptMetadataDTO implements Serializable {

    @ApiModelProperty("类名")
    private String className;

    @ApiModelProperty("方法名")
    private String methodName;

    @ApiModelProperty("参数类型")
    private Class<?>[] paramTypes;

    @ApiModelProperty("参数值")
    private Object[] args;
}
