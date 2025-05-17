package com.zhouzhou.cloud.websocketservice.hanlder;

import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import jakarta.annotation.Resource;

public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;

            // 无读事件（心跳超时），关闭连接并清除在线状态
            if (e.state() == IdleState.READER_IDLE) {

                // 清除本机Channel - PlatformUser映射关系
                String userPlatformUniqueInfo = ChannelConfig.getPlatformUserIdByChannel(ctx.channel());

                // 如果存在PlatformUser与netty服务器的映射关系 说明当前是在线状态 需要移除来表示用户已经下线
                redisUtil.delete("user:route:" + userPlatformUniqueInfo);
                ChannelConfig.removeChannelUser(ctx.channel());

                // 清除本机PlatformUser -Channel映射关系
                ChannelConfig.removeChannel(userPlatformUniqueInfo);

                // 关闭通道连接
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
