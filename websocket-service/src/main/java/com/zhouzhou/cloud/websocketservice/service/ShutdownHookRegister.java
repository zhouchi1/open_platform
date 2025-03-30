package com.zhouzhou.cloud.websocketservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetAddress;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.NODE_CHANNEL_USER_INFO;
import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.WS_NODE_STATUS;

@Slf4j
@RefreshScope
@Component
public class ShutdownHookRegister {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${websocket.port.nodeAPort}")
    private Integer port;

    @PostConstruct
    public void registerHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                String nodeKey = InetAddress.getLocalHost().getHostAddress() + ":" + port;
                log.warn("强制关闭时清理节点: {}", nodeKey);

                stringRedisTemplate.delete(NODE_CHANNEL_USER_INFO + nodeKey);
                stringRedisTemplate.opsForSet().remove(WS_NODE_STATUS, nodeKey);
            } catch (Exception e) {
                log.error("ShutdownHook 清理失败", e);
            }
        }));
    }
}
