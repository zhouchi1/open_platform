package com.zhouzhou.cloud.websocketservice.service;

import com.zhouzhou.cloud.common.resp.BaseListResp;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.resp.AllUserInfoResp;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 用户信息服务层
 */
@Service
public class UserService {

    /**
     * 获取所有当前在线用户信息
     * @return 所有当前在线用户信息
     */
    public BaseListResp<AllUserInfoResp> getAllCurrentUser() {
        // 获取分布式模式下所有节点

    }
}
