package com.zhouzhou.cloud.websocketservice.hanlder;

import com.zhouzhou.cloud.common.service.interfaces.AuthServiceApi;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.dto.SecurityCheckCompleteDTO;
import com.zhouzhou.cloud.websocketservice.utils.AttributeKeyUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

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
public class AuthHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @DubboReference(version = "1.0.0", loadbalance = "roundrobin")
    private AuthServiceApi authServiceApi;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        String token = getTokenFromRequest(request);
        if (authServiceApi.checkTokenValidity(token)) {
            String userId = authServiceApi.queryUserIdByToken(token);
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
                securityCheckCompleteDTO.setUserId(userId);
                securityCheckCompleteDTO.setConnectTime(LocalDateTime.now());
                ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).set(securityCheckCompleteDTO);

                ChannelConfig.addChannel(userId, ctx.channel());
            }

            try {
                pushOfflineMessages(userId, ctx.channel());
                log.info("用户 {} 已上线", userId);
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

}
