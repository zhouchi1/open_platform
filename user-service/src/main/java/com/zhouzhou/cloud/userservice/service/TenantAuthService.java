package com.zhouzhou.cloud.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouzhou.cloud.common.entity.TenantAuth;
import com.zhouzhou.cloud.common.resp.BaseListResp;
import com.zhouzhou.cloud.common.resp.BasePageListResp;
import com.zhouzhou.cloud.userservice.dto.ProcessSaasPlatformInfoDTO;
import com.zhouzhou.cloud.userservice.dto.QuerySaasPlatformDTO;
import com.zhouzhou.cloud.userservice.req.SaasPlatformCreateReq;
import com.zhouzhou.cloud.userservice.resp.QuerySaasPlatformAuthResp;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-29
 * @Description: 租户信息服务层接口
 */
public interface TenantAuthService extends IService<TenantAuth> {


    void createSaasPlatform(SaasPlatformCreateReq saasPlatformCreateReq);

    void saasPlatformProcess(ProcessSaasPlatformInfoDTO processSaasPlatformInfoDTO, String appId);

    QuerySaasPlatformAuthResp querySaasPlatformAuthInfo(QuerySaasPlatformDTO querySaasPlatformDTO, String appId);
}
