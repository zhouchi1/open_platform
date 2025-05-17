package com.zhouzhou.cloud.websocketservice.hanlder;

import com.zhouzhou.cloud.common.dto.UserIdentityInfoDTO;
import com.zhouzhou.cloud.common.service.interfaces.AuthServiceApi;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.SecurityCheckCompleteDTO;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.ssl.SslHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.time.LocalDateTime;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;
import static com.zhouzhou.cloud.websocketservice.utils.AttributeKeyUtils.*;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 身份验证处理器
 */
@Slf4j
@RefreshScope
@Component
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<FullHttpRequest> implements ApplicationListener<WebServerInitializedEvent> {

    @DubboReference(version = "1.0.0", loadbalance = "roundrobin")
    private AuthServiceApi authServiceApi;

    @Resource
    private RedisUtil redisUtil;

    private int port;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        String token = getTokenFromRequest(request);
        if (authServiceApi.checkTokenValidity(token)) {

            UserIdentityInfoDTO userPlatformUniqueInfo = authServiceApi.queryUserIdentityByToken(token);
            ctx.fireChannelRead(request.retain());

            // 创建WebSocket握手工厂
            final WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    getWebSocketLocation(ctx.pipeline(), request, WEBSOCKET_URL), SUB_PROTOCOLS,
                    ALLOW_EXTENSIONS);
            final WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                // 设置自定义头部信息
                HttpHeaders httpHeaders = new DefaultHttpHeaders().add(SEC_WEBSOCKET_PROTOCOLS, token);
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
                securityCheckCompleteDTO.setUserPlatformUniqueInfo(userPlatformUniqueInfo.getUserSaasPlatformType() + ":" + userPlatformUniqueInfo.getUserId());
                securityCheckCompleteDTO.setConnectTime(LocalDateTime.now());
                ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).set(securityCheckCompleteDTO);

                ChannelConfig.addChannel(userPlatformUniqueInfo.getUserSaasPlatformType() + ":" + userPlatformUniqueInfo.getUserId(), ctx.channel());
                ChannelConfig.bindChannelUser(ctx.channel(), userPlatformUniqueInfo.getUserSaasPlatformType() + ":" + userPlatformUniqueInfo.getUserId());
            }

            try {
                pushOfflineMessages(userPlatformUniqueInfo.getUserSaasPlatformType() + ":" + userPlatformUniqueInfo.getUserId(), ctx.channel());

                // 注意 当用户主动断开websocket连接后（缓存中删除用户在线状态） 再次使用未过期的token进行登录 需要在缓存中再次添加在线状态
                if (ObjectUtils.isEmpty(redisUtil.get(userPlatformUniqueInfo.getUserSaasPlatformType() + ":" + userPlatformUniqueInfo.getUserId()))) {
                    redisUtil.set(userPlatformUniqueInfo.getUserSaasPlatformType() + ":" + userPlatformUniqueInfo.getUserId(), InetAddress.getLocalHost().getHostAddress() + ":" + getPort(), 3600);
                }
                log.info("【Saas Platform->{}】,【User->{}】 Status Up！", userPlatformUniqueInfo.getUserSaasPlatformType(), userPlatformUniqueInfo.getUserId());
            } catch (Exception e) {
                log.error("用户信息获取失败，关闭连接", e);
                ctx.close();
            }

        } else {
            sendUnauthorizedResponse(ctx);
        }
    }

    private String getTokenFromRequest(FullHttpRequest request) {
        return request.headers().get(SEC_WEBSOCKET_PROTOCOLS);
    }

    private void sendUnauthorizedResponse(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = WS;
        if (cp.get(SslHandler.class) != null) {
            protocol = WSS;
        }
        String host = req.headers().get(HttpHeaderNames.HOST);
        return protocol + "://" + host + path;
    }

    private void pushOfflineMessages(String userId, Channel channel) {
//        Long messageSize = stringRedisTemplate.opsForHash().size(OFFLINE_MESSAGE_BY_USER + userId);

//        if (messageSize == 0) {
//            log.info("用户 {} 没有离线消息", userId);
//            return;
//        }

//        Map<Object, Object> message = stringRedisTemplate.opsForHash().entries(OFFLINE_MESSAGE_BY_USER + userId);

//        message.forEach((key, value) -> channel.writeAndFlush(new TextWebSocketFrame((String) value)));

//        log.info("用户 {} 的 {} 条离线消息已推送", userId, messageSize);
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.port = event.getWebServer().getPort();
    }

    public int getPort() {
        return this.port;
    }
}
