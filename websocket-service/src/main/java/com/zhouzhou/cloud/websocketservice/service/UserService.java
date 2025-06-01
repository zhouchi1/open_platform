package com.zhouzhou.cloud.websocketservice.service;

import com.zhouzhou.cloud.common.resp.BaseListResp;
import com.zhouzhou.cloud.websocketservice.resp.AllUserNodeResp;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 用户信息服务层
 */
@Service
public class UserService {

    public BaseListResp<AllUserNodeResp> getAllCurrentUser() {

        // 查询Nacos注册中心中 注册的websocket-service服务的所有节点

        // 根据节点信息查询 所对应的所有在线用户

        // 查询详情信息

        return null;
    }
}
