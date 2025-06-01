package com.zhouzhou.cloud.userservice.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zhouzhou.cloud.common.service.base.ResponseData;
import com.zhouzhou.cloud.common.utils.ResponseDataUtil;
import com.zhouzhou.cloud.userservice.annotation.SignPayloadType;
import com.zhouzhou.cloud.userservice.annotation.SignSaasIdentity;
import com.zhouzhou.cloud.userservice.dto.ProcessSaasUserInfoDTO;
import com.zhouzhou.cloud.userservice.req.ProcessSaasReq;
import com.zhouzhou.cloud.userservice.service.UserInfoService;
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
 * @Description: saas平台用户入驻接口
 */
@Api(tags = "用户微服务 >> Saas用户")
@RestController
@RequestMapping("/saasUser")
public class CreateSaasUserController {

    @Resource
    private UserInfoService userInfoService;

    @SignSaasIdentity
    @SignPayloadType(ProcessSaasUserInfoDTO.class)
    @ApiOperation("处理Saas平台用户信息（支持批量）(对外接口)")
    @ApiOperationSupport(author = "Sr.Zhou")
    @PostMapping("/processSaasUser")
    public ResponseData<?> processSaasUser(@RequestBody @Validated ProcessSaasReq<ProcessSaasUserInfoDTO> processSaasReq) {
        ProcessSaasUserInfoDTO processSaasUserInfoDTO = processSaasReq.getParsedPayload();
        userInfoService.processSaasUser(processSaasUserInfoDTO, processSaasReq.getAppId());
        return ResponseDataUtil.success();
    }
}
