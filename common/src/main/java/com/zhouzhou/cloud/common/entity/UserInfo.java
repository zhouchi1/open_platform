package com.zhouzhou.cloud.common.entity;

import com.zhouzhou.cloud.common.service.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-16
 * @Description: 用户表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends BaseEntity {


    private static final long serialVersionUID = 2345782365776235764L;

    /**
     * saas平台内部唯一标识
     */
    private String appId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 是否启用状态
     */
    private Boolean status;
}