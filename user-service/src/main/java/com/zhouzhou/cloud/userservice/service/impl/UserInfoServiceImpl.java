package com.zhouzhou.cloud.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.common.utils.BizExUtil;
import com.zhouzhou.cloud.userservice.dto.CreateSaasUserInfosDTO;
import com.zhouzhou.cloud.userservice.dto.ProcessSaasUserInfoDTO;
import com.zhouzhou.cloud.userservice.enums.ProcessContinueEnum;
import com.zhouzhou.cloud.userservice.enums.ProcessTypeEnum;
import com.zhouzhou.cloud.common.mapper.UserInfoMapper;
import com.zhouzhou.cloud.userservice.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processSaasUser(ProcessSaasUserInfoDTO processSaasUserInfoDTO, String appId) {

        BizExUtil.requirefalse(ObjectUtils.isEmpty(processSaasUserInfoDTO.getCreateSaasUserInfosDTOList()), "要处理的平台用户信息列表为空，请核验后请求");

        ProcessTypeEnum type = ProcessTypeEnum.fromCode(processSaasUserInfoDTO.getProcessType());

        // 查询要添加的用户是否已经在系统中存在 如果存在则查出来
        Map<String, UserInfo> userInfoList = baseMapper.selectList(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getAppId, appId)).stream().collect(Collectors.toMap(UserInfo::getUserName, userInfo -> userInfo));

        ProcessContinueEnum processContinueEnum = ProcessContinueEnum.fromCode(processSaasUserInfoDTO.getProcessType());


        // 根据不同的类型走不同的逻辑
        switch (type) {
            case ADD:
                for (CreateSaasUserInfosDTO createSaasUserInfosDTO : processSaasUserInfoDTO.getCreateSaasUserInfosDTOList()) {
                    UserInfo oldUserInfo = userInfoList.get(createSaasUserInfosDTO.getUserName());
                    if (!ObjectUtils.isEmpty(oldUserInfo)) {

                        if (processContinueEnum.equals(ProcessContinueEnum.SKIP)) {
                            continue;
                        } else {
                            BizExUtil.requirefalse(true, "处理失败，已有的saas平台用户信息！" + oldUserInfo.getUserName());
                        }
                    }

                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserName(createSaasUserInfosDTO.getUserName());
                    userInfo.setAppId(appId);
                    baseMapper.insert(userInfo);
                }
                break;
            case DELETE:
                for (CreateSaasUserInfosDTO createSaasUserInfosDTO : processSaasUserInfoDTO.getCreateSaasUserInfosDTOList()) {
                    UserInfo oldUserInfo = userInfoList.get(createSaasUserInfosDTO.getUserName());
                    if (ObjectUtils.isEmpty(oldUserInfo)) {
                        if (processContinueEnum.equals(ProcessContinueEnum.SKIP)) {
                            continue;
                        } else {
                            BizExUtil.requirefalse(true, "处理失败，未找到saas平台用户信息！：" + createSaasUserInfosDTO.getUserName());
                        }
                    }
                    baseMapper.deleteById(oldUserInfo.getId());
                }
                break;
            case ENABLE:
                for (CreateSaasUserInfosDTO createSaasUserInfosDTO : processSaasUserInfoDTO.getCreateSaasUserInfosDTOList()) {
                    UserInfo oldUserInfo = userInfoList.get(createSaasUserInfosDTO.getUserName());
                    if (ObjectUtils.isEmpty(oldUserInfo)) {
                        if (processContinueEnum.equals(ProcessContinueEnum.SKIP)) {
                            continue;
                        } else {
                            BizExUtil.requirefalse(true, "处理失败，未找到saas平台用户信息！：" + createSaasUserInfosDTO.getUserName());
                        }
                    }
                    oldUserInfo.setStatus(true);
                    baseMapper.updateById(oldUserInfo);
                }
                break;
            case DISABLE:
                for (CreateSaasUserInfosDTO createSaasUserInfosDTO : processSaasUserInfoDTO.getCreateSaasUserInfosDTOList()) {
                    UserInfo oldUserInfo = userInfoList.get(createSaasUserInfosDTO.getUserName());
                    if (ObjectUtils.isEmpty(oldUserInfo)) {
                        if (processContinueEnum.equals(ProcessContinueEnum.SKIP)) {
                            continue;
                        } else {
                            BizExUtil.requirefalse(true, "处理失败，未找到saas平台用户信息！：" + createSaasUserInfosDTO.getUserName());
                        }
                    }
                    oldUserInfo.setStatus(false);
                    baseMapper.updateById(oldUserInfo);
                }
        }
    }
}
