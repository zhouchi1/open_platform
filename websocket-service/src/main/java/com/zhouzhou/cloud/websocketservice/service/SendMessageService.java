package com.zhouzhou.cloud.websocketservice.service;

import com.alibaba.fastjson2.JSON;
import com.zhouzhou.cloud.common.dto.MessageDTO;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.rabbitmqproducer.RabbitMqSenderApi;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendMessageService {

    @Resource
    private RabbitMqSenderApi rabbitMqSenderApi;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 发送消息给指定用户
     *
     * @param messageDTO 消息传输体
     */
    public void sendMessage(MessageDTO messageDTO) {
        // 查找当前用户对应的通道 找到后直接进行消息的发送
        Channel channel = ChannelConfig.getChannel(messageDTO.getTargetUserSaasPlatformType() + ":" + messageDTO.getTargetUserId());

        // netty服务端以外失去与终端用户的连接
        if (channel == null) {
            // 将消息加入到MQ中 削峰
            rabbitMqSenderApi.sendTopicMessage("topicExchange", "topic.routing.key1", JSON.toJSONString(messageDTO.getMessage()));

            // 再次删除Redis中终端用户与服务器的映射关系
            redisUtil.delete("user:route:" + messageDTO.getTargetUserSaasPlatformType() + ":" + messageDTO.getTargetUserId());
            return;
        }

        // 将消息发送给终端用户
        channel.writeAndFlush(new TextWebSocketFrame(messageDTO.getMessage()));
    }
}
