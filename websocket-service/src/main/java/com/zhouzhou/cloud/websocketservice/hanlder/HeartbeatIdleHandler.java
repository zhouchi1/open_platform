package com.zhouzhou.cloud.websocketservice.hanlder;

import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class HeartbeatIdleHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                String userPlatformUniqueInfo = ChannelConfig.getPlatformUserIdByChannel(ctx.channel());
                try {
                    // 删除所有证明用户在线的遗留信息
                    if (userPlatformUniqueInfo != null) {
                        redisUtil.delete(userPlatformUniqueInfo);
                        ChannelConfig.removeChannelUser(ctx.channel());
                        ChannelConfig.removeChannel(userPlatformUniqueInfo);
                    }
                } finally {
                    ctx.close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
