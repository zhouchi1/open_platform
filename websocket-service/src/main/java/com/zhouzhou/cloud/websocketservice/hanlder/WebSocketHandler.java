package com.zhouzhou.cloud.websocketservice.hanlder;

import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.utils.AttributeKeyUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 客户端消息处理
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Resource
    private RedisUtil redisUtil;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {


        super.channelInactive(ctx);

        // 清除本机Channel - PlatformUser映射关系
        String userPlatformUniqueInfo = ChannelConfig.getPlatformUserIdByChannel(ctx.channel());

        // 如果为空说明 映射关系已被清除 无需再次处理（已经被当作是空闲状态被清除）
        if (ObjectUtils.isEmpty(userPlatformUniqueInfo)){
            return;
        }

        String[] parts = userPlatformUniqueInfo.split(":");
        String platform = parts[0];
        String user = parts[1];

        log.info("【Saas Platform->{}】,【User->{}】 Status Down！", platform, user);

        // 删除用户在线状态
        redisUtil.delete(userPlatformUniqueInfo + "status");
        ChannelConfig.removeChannelUser(ctx.channel());

        // 清除本机PlatformUser映射关系 -Channel映射关系
        ChannelConfig.removeChannel(userPlatformUniqueInfo);

        // 删除用户连接信息
        AttributeKeyUtils.removeSecurityCheckCompleteAttributeKey(ctx);

        ctx.close();
    }
}
