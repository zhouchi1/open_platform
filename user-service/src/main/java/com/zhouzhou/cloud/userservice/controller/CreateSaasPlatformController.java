package com.zhouzhou.cloud.userservice.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import com.zhouzhou.cloud.common.service.base.ResponseData;
import com.zhouzhou.cloud.common.utils.ResponseDataUtil;
import com.zhouzhou.cloud.userservice.annotation.SignPayloadType;
import com.zhouzhou.cloud.userservice.annotation.SignSaasIdentity;
import com.zhouzhou.cloud.userservice.dto.ProcessSaasPlatformInfoDTO;
import com.zhouzhou.cloud.userservice.dto.QuerySaasPlatformDTO;
import com.zhouzhou.cloud.userservice.req.ProcessSaasReq;
import com.zhouzhou.cloud.userservice.req.SaasPlatformCreateReq;
import com.zhouzhou.cloud.userservice.resp.QuerySaasPlatformAuthResp;
import com.zhouzhou.cloud.userservice.service.TenantAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-29
 * @Description: saas平台对外接口
 */
@Api(tags = "用户微服务 >> Saas平台")
@RestController
@RequestMapping("/saasPlatform")
public class CreateSaasPlatformController {

    @Resource
    private TenantAuthService tenantAuthService;

    @ApiOperation("创建Saas平台授权")
    @ApiOperationSupport(author = "Sr.Zhou")
    @PostMapping("/createSaasPlatform")
    public ResponseData<?> createSaasPlatform(@RequestBody @Validated SaasPlatformCreateReq saasPlatformCreateReq) {
        tenantAuthService.createSaasPlatform(saasPlatformCreateReq);
        return ResponseDataUtil.success();
    }

    @SignSaasIdentity
    @SignPayloadType(ProcessSaasPlatformInfoDTO.class)
    @ApiOperation("Saas授权平台操作")
    @ApiOperationSupport(author = "Sr.Zhou")
    @PostMapping("/saasPlatformProcess")
    public ResponseData<?> saasPlatformProcess(@RequestBody @Validated ProcessSaasReq<ProcessSaasPlatformInfoDTO> processSaasReq) {
        ProcessSaasPlatformInfoDTO processSaasPlatformInfoDTO = processSaasReq.getParsedPayload();
        tenantAuthService.saasPlatformProcess(processSaasPlatformInfoDTO, processSaasReq.getAppId());
        return ResponseDataUtil.success();
    }

    @SignSaasIdentity
    @SignPayloadType(QuerySaasPlatformDTO.class)
    @ApiOperation("查询Saas平台以及下设用户的授权信息")
    @ApiOperationSupport(author = "Sr.Zhou")
    @PostMapping("/querySaasPlatformAuthInfo")
    public ResponseData<QuerySaasPlatformAuthResp> querySaasPlatformAuthInfo(@RequestBody @Validated ProcessSaasReq<QuerySaasPlatformDTO> processSaasReq) {
        QuerySaasPlatformDTO querySaasPlatformDTO = processSaasReq.getParsedPayload();
        return ResponseDataUtil.success(tenantAuthService.querySaasPlatformAuthInfo(querySaasPlatformDTO, processSaasReq.getAppId()));
    }
}
