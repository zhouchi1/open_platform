package com.zhouzhou.cloud.websocketservice.service;

import cn.hutool.core.collection.CollectionUtil;
import com.zhouzhou.cloud.common.resp.BaseListResp;
import com.zhouzhou.cloud.websocketservice.resp.AllUserInfoResp;
import com.zhouzhou.cloud.websocketservice.resp.AllUserNodeResp;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
     *
     * @return 所有当前在线用户信息
     */
    public BaseListResp<AllUserNodeResp> getAllCurrentUser() {

        // 获取分布式模式下所有节点
        Set<String> allNode = stringRedisTemplate.opsForSet().members(WS_NODE_STATUS);

        if (CollectionUtil.isEmpty(allNode)) {
            return BaseListResp.build(new ArrayList<>());
        }

        List<AllUserNodeResp> allUserNodeRespList = new ArrayList<>(allNode.size());
        allNode.forEach(node -> {
            Map<Object, Object> allUserInfo = stringRedisTemplate.opsForHash().entries(NODE_CHANNEL_USER_INFO + node);
            if (CollectionUtil.isEmpty(allUserInfo)) {
                return;
            }
            AllUserNodeResp allUserNodeResp = new AllUserNodeResp();
            List<AllUserInfoResp> allUserNodeRespArrayList = new ArrayList<>();
            allUserNodeResp.setNodeInfo(node);
            allUserInfo.forEach((channelId, userId) -> {
                AllUserInfoResp allUserInfoResp = new AllUserInfoResp((String) userId, (String) channelId);
                allUserNodeRespArrayList.add(allUserInfoResp);
            });
            allUserNodeResp.setNodeAllUserInfoRespList(allUserNodeRespArrayList);
            allUserNodeRespList.add(allUserNodeResp);
        });

        return BaseListResp.build(allUserNodeRespList);
    }
}
