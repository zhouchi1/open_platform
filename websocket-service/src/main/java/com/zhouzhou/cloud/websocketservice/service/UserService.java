package com.zhouzhou.cloud.websocketservice.service;

import com.zhouzhou.cloud.common.resp.BaseListResp;
import com.zhouzhou.cloud.websocketservice.resp.AllUserInfoResp;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 用户信息服务层
 */
@Service
public class UserService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 获取所有当前在线用户信息
     * @return 所有当前在线用户信息
     */
    public BaseListResp<AllUserInfoResp> getAllCurrentUser() {

        // 获取分布式模式下所有节点
        Set<String> allUser = stringRedisTemplate.keys(CHANNEL_ID + "*");

        if (allUser == null) {
            return BaseListResp.build(new ArrayList<>());
        }

        List<AllUserInfoResp> allUserInfoRespList = new ArrayList<>(allUser.size());
        for (String user : allUser) {
            String userId = stringRedisTemplate.opsForValue().get(user);
            AllUserInfoResp allUserInfoResp = new AllUserInfoResp(userId, user);
            allUserInfoRespList.add(allUserInfoResp);
        }

        return BaseListResp.build(allUserInfoRespList);
    }
}
