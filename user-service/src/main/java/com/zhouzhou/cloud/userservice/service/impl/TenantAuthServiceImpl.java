package com.zhouzhou.cloud.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zhouzhou.cloud.common.entity.TenantAuth;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.userservice.dto.ProcessSaasPlatformInfoDTO;
import com.zhouzhou.cloud.userservice.enums.ProcessTypeEnum;
import com.zhouzhou.cloud.common.mapper.TenantAuthMapper;
import com.zhouzhou.cloud.userservice.dto.QuerySaasPlatformDTO;
import com.zhouzhou.cloud.userservice.req.SaasPlatformCreateReq;
import com.zhouzhou.cloud.userservice.resp.QuerySaasPlatformAuthDetailResp;
import com.zhouzhou.cloud.userservice.resp.QuerySaasPlatformAuthResp;
import com.zhouzhou.cloud.userservice.service.TenantAuthService;
import com.zhouzhou.cloud.userservice.service.UserInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TenantAuthServiceImpl extends ServiceImpl<TenantAuthMapper, TenantAuth>
        implements TenantAuthService {

    @Resource
    private UserInfoService userInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSaasPlatform(SaasPlatformCreateReq saasPlatformCreateReq) {

        TenantAuth newTenantAuth = new TenantAuth();
        newTenantAuth.setAppId(UUID.randomUUID().toString());
        newTenantAuth.setAppPublicSecret(saasPlatformCreateReq.getAppPublicSecret());
        newTenantAuth.setStatus(true);
        baseMapper.insert(newTenantAuth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saasPlatformProcess(ProcessSaasPlatformInfoDTO processSaasPlatformInfoDTO, String appId) {

        ProcessTypeEnum type = ProcessTypeEnum.fromCode(processSaasPlatformInfoDTO.getProcessType());

        TenantAuth tenantAuth = baseMapper.selectOne(new LambdaQueryWrapper<TenantAuth>().eq(TenantAuth::getAppId, appId));

        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getAppId, appId));

        switch (type) {
            case DELETE:
                // 删除Saas平台租户信息
                baseMapper.deleteById(tenantAuth);

                // 删除Saas平台下的所有用户信息
                userInfoService.removeBatchByIds(userInfoList);
                break;
            case ENABLE:
                // 将Saas平台租户状态设置为开启
                tenantAuth.setStatus(true);
                userInfoService.updateBatchById(userInfoList);
                break;
            case DISABLE:
                // 将Saas平台租户状态设置为关闭
                tenantAuth.setStatus(false);
                userInfoService.updateBatchById(userInfoList);
        }
    }

    @Override
    public QuerySaasPlatformAuthResp querySaasPlatformAuthInfo(QuerySaasPlatformDTO querySaasPlatformDTO, String appId) {

        QuerySaasPlatformAuthResp querySaasPlatformAuthResp = new QuerySaasPlatformAuthResp();

        // 查询Saas平台授权信息
        TenantAuth tenantAuth = baseMapper.selectOne(new LambdaQueryWrapper<TenantAuth>().eq(TenantAuth::getAppId, appId));

        querySaasPlatformAuthResp.setAppId(tenantAuth.getAppId());
        querySaasPlatformAuthResp.setStatus(tenantAuth.getStatus());

        // 查询该Saas平台下所有用户的授权信息
        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getAppId, appId));

        if (CollectionUtils.isEmpty(userInfoList)) {
            querySaasPlatformAuthResp.setQuerySaasPlatformAuthDetailRespList(Collections.emptyList());
        } else {
            querySaasPlatformAuthResp.setQuerySaasPlatformAuthDetailRespList(userInfoList.stream().map(userInfo -> {
                QuerySaasPlatformAuthDetailResp querySaasPlatformAuthDetailResp = new QuerySaasPlatformAuthDetailResp();
                querySaasPlatformAuthDetailResp.setUserName(userInfo.getUserName());
                querySaasPlatformAuthDetailResp.setStatus(userInfo.getStatus());
                return querySaasPlatformAuthDetailResp;
            }).collect(Collectors.toList()));
        }

        return querySaasPlatformAuthResp;
    }
}


