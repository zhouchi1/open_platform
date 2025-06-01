package com.zhouzhou.cloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-19
 * @Description: 程序中断元数据传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterruptMetadataDTO implements Serializable {

    private String className;

    private String methodName;

    private String methodChineseName;

    private Class<?>[] parameterTypes;

    private List<ParamInfoDTO> args;
}
