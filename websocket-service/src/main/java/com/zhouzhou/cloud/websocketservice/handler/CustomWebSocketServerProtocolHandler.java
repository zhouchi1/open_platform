package com.zhouzhou.cloud.websocketservice.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;

import java.util.List;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-08-30
 * @Description: 重写webSocket处理器
 */
public class CustomWebSocketServerProtocolHandler extends CustomWebSocketProtocolHandler {

    /**
     * Events that are fired to notify about handshake status
     */
    public enum ServerHandshakeStateEvent {
        /**
         * The Handshake was completed successfully and the channel was upgraded to websockets.
         *
         * @deprecated in favor of {@link WebSocketServerProtocolHandler.HandshakeComplete} class,
         * it provides extra information about the handshake
         */
        @Deprecated
        HANDSHAKE_COMPLETE
    }

    /**
     * The Handshake was completed successfully and the channel was upgraded to websockets.
     */
    public static final class HandshakeComplete {
        private final String requestUri;
        private final HttpHeaders requestHeaders;
        private final String selectedSubProtocol;

        public HandshakeComplete(String requestUri, HttpHeaders requestHeaders, String selectedSubProtocol) {
            this.requestUri = requestUri;
            this.requestHeaders = requestHeaders;
            this.selectedSubProtocol = selectedSubProtocol;
        }

        public String requestUri() {
            return requestUri;
        }

        public HttpHeaders requestHeaders() {
            return requestHeaders;
        }

        public String selectedSubProtocol() {
            return selectedSubProtocol;
        }
    }

    private static final AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY =
            AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");

    private final String websocketPath;
    private final String subProtocols;
    private final boolean allowExtensions;
    private final int maxFramePayloadLength;
    private final boolean allowMaskMismatch;
    private final boolean checkStartsWith;

    public CustomWebSocketServerProtocolHandler(String websocketPath) {
        this(websocketPath, null, false);
    }

    public CustomWebSocketServerProtocolHandler(String websocketPath, boolean checkStartsWith) {
        this(websocketPath, null, false, 65536, false, checkStartsWith);
    }

    public CustomWebSocketServerProtocolHandler(String websocketPath, String subProtocols) {
        this(websocketPath, subProtocols, false);
    }

    public CustomWebSocketServerProtocolHandler(String websocketPath, String subProtocols, boolean allowExtensions) {
        this(websocketPath, subProtocols, allowExtensions, 65536);
    }

    public CustomWebSocketServerProtocolHandler(String websocketPath, String subProtocols,
                                                boolean allowExtensions, int maxFrameSize) {
        this(websocketPath, subProtocols, allowExtensions, maxFrameSize, false);
    }

    public CustomWebSocketServerProtocolHandler(String websocketPath, String subProtocols,
                                                boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch) {
        this(websocketPath, subProtocols, allowExtensions, maxFrameSize, allowMaskMismatch, false);
    }

    public CustomWebSocketServerProtocolHandler(String websocketPath, String subProtocols,
                                                boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch, boolean checkStartsWith) {
        this.websocketPath = websocketPath;
        this.subProtocols = subProtocols;
        this.allowExtensions = allowExtensions;
        this.maxFramePayloadLength = maxFrameSize;
        this.allowMaskMismatch = allowMaskMismatch;
        this.checkStartsWith = checkStartsWith;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        ChannelPipeline cp = ctx.pipeline();
        if (cp.get(CustomWebSocketServerProtocolHandler.class) == null) {
            // Add the WebSocketHandshakeHandler before this one.
            ctx.pipeline().addBefore(ctx.name(), CustomWebSocketServerProtocolHandler.class.getName(),
                    new CustomWebSocketServerProtocolHandler(websocketPath, subProtocols,
                            allowExtensions, maxFramePayloadLength, allowMaskMismatch, checkStartsWith));
        }
        if (cp.get(Utf8FrameValidator.class) == null) {
            // Add the UFT8 checking before this one.
            ctx.pipeline().addBefore(ctx.name(), Utf8FrameValidator.class.getName(),
                    new Utf8FrameValidator());
        }
    }

    /**
     * 在该位置中接收到关闭帧的请求 随着close方法的触发(松手) 会继续触发channelInactive方法(从注册的通道组中删除此通道)
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        if (frame instanceof CloseWebSocketFrame) {
            WebSocketServerHandshaker handshaker = getHandshaker(ctx.channel());
            if (handshaker != null) {
                frame.retain();
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame);
            } else {
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
            return;
        }
        super.decode(ctx, frame, out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof WebSocketHandshakeException) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1, HttpResponseStatus.BAD_REQUEST, Unpooled.wrappedBuffer(cause.getMessage().getBytes()));
            ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx.fireExceptionCaught(cause);
            ctx.close();
        }
    }

    /**
     * 获取握手信息
     */
    static WebSocketServerHandshaker getHandshaker(Channel channel) {
        return channel.attr(HANDSHAKER_ATTR_KEY).get();
    }

    /**
     * 设置握手信息
     */
    public static void setHandshaker(Channel channel, WebSocketServerHandshaker handshaker) {
        channel.attr(HANDSHAKER_ATTR_KEY).set(handshaker);
    }
}
