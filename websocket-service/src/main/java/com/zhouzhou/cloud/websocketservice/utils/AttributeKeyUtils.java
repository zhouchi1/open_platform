package com.zhouzhou.cloud.websocketservice.utils;

import com.zhouzhou.cloud.websocketservice.dto.SecurityCheckCompleteDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-30
 * @Description: 内存存储信息
 */
public class AttributeKeyUtils {


    public static final AttributeKey<SecurityCheckCompleteDTO> SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY =
            AttributeKey.valueOf("SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY");

    /**
     * 删除本机器中暂存的用户登录信息
     */
    public static void removeSecurityCheckCompleteAttributeKey(ChannelHandlerContext ctx) {
        ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).remove();
    }

}