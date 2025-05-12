package com.zhouzhou.cloud.websocketservice.service;

import com.zhouzhou.cloud.common.dto.MessageDTO;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendMessageService {


    /**
     * 发送消息给指定用户
     * @param messageDTO 消息传输体
     */
    public void sendMessage(MessageDTO messageDTO) {
        // 查找当前用户对应的通道 找到后直接进行消息的发送
        Channel channel = ChannelConfig.getChannel(messageDTO.getTargetUserId());
        channel.writeAndFlush(new TextWebSocketFrame(messageDTO.getMessage()));
    }
}
