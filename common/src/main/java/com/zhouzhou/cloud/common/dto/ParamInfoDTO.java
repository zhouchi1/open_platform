package com.zhouzhou.cloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-21
 * @Description: 程序中断元数据实体传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamInfoDTO implements Serializable {

    private static final long serialVersionUID = 2178461872643871628L;

    private String jsonValue; // 直接存储带类型信息的JSON
}
