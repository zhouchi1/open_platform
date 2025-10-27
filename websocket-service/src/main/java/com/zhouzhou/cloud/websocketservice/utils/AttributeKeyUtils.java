package com.zhouzhou.cloud.websocketservice.utils;

import com.zhouzhou.cloud.websocketservice.dto.SecurityCheckCompleteDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-30
 * @Description: 内存存储信息
 */
public class AttributeKeyUtils {


    public static final AttributeKey<SecurityCheckCompleteDTO> SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY =
            AttributeKey.valueOf("SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY");

    public static String getUserIdFromChannel(ChannelHandlerContext ctx) {

        if (ctx.channel().hasAttr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY)) {
            SecurityCheckCompleteDTO securityInfo = ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).get();

            if (securityInfo == null) {
                ctx.writeAndFlush(new TextWebSocketFrame("未找到用户认证信息"));
            }

            return securityInfo.getUserId();
        }
        ctx.writeAndFlush(new TextWebSocketFrame("未找到用户认证信息"));
        return null;
    }
}