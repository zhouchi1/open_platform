package com.zhouzhou.cloud.websocketservice.handler;

import com.zhouzhou.cloud.common.dto.UserIdentityInfoDTO;
import com.zhouzhou.cloud.common.service.interfaces.AuthRpcServer;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.constant.ConnectConstants;
import com.zhouzhou.cloud.websocketservice.dto.SecurityCheckCompleteDTO;
import com.zhouzhou.cloud.websocketservice.utils.AttributeKeyUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.ssl.SslHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 身份验证处理器
 */

@Slf4j
@Component
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Value("${websocket.port}")
    private Integer port;

    @DubboReference(version = "1.0.0", loadbalance = "roundrobin")
    private AuthRpcServer authRpcServer;

    @Resource
    private RedisUtil redisUtil;

    private final StringRedisTemplate stringRedisTemplate;

    public AuthHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws UnknownHostException {

        String token = getTokenFromRequest(request);
        if (authRpcServer.checkTokenValidity(token)) {

            UserIdentityInfoDTO userPlatformUniqueInfo = authRpcServer.queryUserIdentityByToken(token);
            ctx.fireChannelRead(request.retain());

            // 创建WebSocket握手工厂
            final WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    getWebSocketLocation(ctx.pipeline(), request, ConnectConstants.WEBSOCKET_URL), ConnectConstants.SUB_PROTOCOLS,
                    ConnectConstants.ALLOW_EXTENSIONS);
            final WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                // 设置自定义头部信息
                HttpHeaders httpHeaders = new DefaultHttpHeaders().add(ConnectConstants.SEC_WEBSOCKET_PROTOCOLS, token);
                // 执行握手 握手完成后会触发channelActive方法
                final ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), request, httpHeaders, ctx.channel().newPromise());
                handshakeFuture.addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        ctx.fireExceptionCaught(future.cause());
                    } else {
                        // 握手成功
                        ctx.fireUserEventTriggered(
                                CustomWebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
                        ctx.fireUserEventTriggered(
                                new CustomWebSocketServerProtocolHandler.HandshakeComplete(
                                        request.uri(), request.headers(), handshaker.selectedSubprotocol()));
                    }
                });
                // 绑定握手与通道
                CustomWebSocketServerProtocolHandler.setHandshaker(ctx.channel(), handshaker);

                // 将登录信息存储在本机内存中
                SecurityCheckCompleteDTO securityCheckCompleteDTO = new SecurityCheckCompleteDTO();
                securityCheckCompleteDTO.setChannelId(ctx.channel().id().asLongText());
                securityCheckCompleteDTO.setUserId(userPlatformUniqueInfo.getUserId());
                securityCheckCompleteDTO.setConnectTime(LocalDateTime.now());
                securityCheckCompleteDTO.setUserCode(userPlatformUniqueInfo.getUserCode());
                ctx.channel().attr(AttributeKeyUtils.SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).set(securityCheckCompleteDTO);

                // 移除同一用户已登录的channel
                String channelId = (String) redisUtil.hGet(ConnectConstants.NODE_CHANNEL_USER_INFO + InetAddress.getLocalHost().getHostAddress() + ":" + port, userPlatformUniqueInfo.getUserId());

                if (!ObjectUtils.isEmpty(channelId)) {

                    // 此处若以后分布式部署 如果没有找到本机内存中的channel 需要将channelId广播到其他的节点进行强制下线操作
                    Channel channel = ChannelConfig.getChannel(channelId);
                    if (!ObjectUtils.isEmpty(channel)) {
                        channel.close();
                    }
                }

                ChannelConfig.addChannel(ctx.channel().id().asLongText(), ctx.channel());

                log.info("将连接信息放入到redis中，缓存信息为：" + ConnectConstants.NODE_CHANNEL_USER_INFO + InetAddress.getLocalHost().getHostAddress() + "," + port + "," + userPlatformUniqueInfo.getUserId() + "," + ctx.channel().id().asLongText());

                redisUtil.hSet(ConnectConstants.NODE_CHANNEL_USER_INFO + InetAddress.getLocalHost().getHostAddress() + ":" + port, userPlatformUniqueInfo.getUserId(), ctx.channel().id().asLongText());
            }

            try {
                pushOfflineMessages(userPlatformUniqueInfo.getUserId(), ctx.channel());
                log.info("用户 {} 已上线", userPlatformUniqueInfo.getUserId());
            } catch (Exception e) {
                log.error("用户信息获取失败，关闭连接", e);
                ctx.close();
            }

        } else {
            sendUnauthorizedResponse(ctx);
        }
    }

    private String getTokenFromRequest(FullHttpRequest request) {
        return request.headers().get(ConnectConstants.SEC_WEBSOCKET_PROTOCOLS);
    }

    private void sendUnauthorizedResponse(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = ConnectConstants.WS;
        if (cp.get(SslHandler.class) != null) {
            protocol = ConnectConstants.WSS;
        }
        String host = req.headers().get(HttpHeaderNames.HOST);
        return protocol + "://" + host + path;
    }

    private void pushOfflineMessages(String userId, Channel channel) {
        Long messageSize = stringRedisTemplate.opsForHash().size(ConnectConstants.OFFLINE_MESSAGE_BY_USER + userId);

        if (messageSize == 0) {
            return;
        }

        Map<Object, Object> message = stringRedisTemplate.opsForHash().entries(ConnectConstants.OFFLINE_MESSAGE_BY_USER + userId);

        message.forEach((key, value) -> channel.writeAndFlush(new TextWebSocketFrame((String) value)).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                redisUtil.hDelete(ConnectConstants.OFFLINE_MESSAGE_BY_USER + userId, key);
            }
        }));

        log.info("用户 {} 的 {} 条离线消息已推送", userId, messageSize);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
