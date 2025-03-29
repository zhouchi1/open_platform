package com.zhouzhou.cloud.websocketservice.hanlder;

import com.zhouzhou.cloud.websocketservice.service.TokenService;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.ssl.SslHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.zhouzhou.cloud.websocketservice.constant.ConnectConstants.*;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 身份验证处理器
 */

@RefreshScope
@Component
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Value("${websocket.node.nodeANum}")
    private Integer nodeNum;

    private final TokenService tokenService;

    private final StringRedisTemplate stringRedisTemplate;

    public AuthHandler(TokenService tokenService, StringRedisTemplate stringRedisTemplate) {
        this.tokenService = tokenService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        String token = getTokenFromRequest(request);
        if (tokenService.validateToken(token)) {
            String userId = tokenService.getUserIdFromToken(token);
            stringRedisTemplate.opsForValue().set(NODE_ID + nodeNum + CHANNEL_ID + ctx.channel().id().asLongText(), USER + userId);
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
            }

        } else {
            sendUnauthorizedResponse(ctx);
        }
    }

    private String getTokenFromRequest(FullHttpRequest request) {
        return request.headers().get("Sec-WebSocket-Protocol");
    }

    private void sendUnauthorizedResponse(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = "ws";
        if (cp.get(SslHandler.class) != null) {
            protocol = "wss";
        }
        String host = req.headers().get(HttpHeaderNames.HOST);
        return protocol + "://" + host + path;
    }
}
