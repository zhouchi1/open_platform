package com.zhouzhou.cloud.websocketservice.constant;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-10
 * @Description: WebSocket常量类
 */
public class ConnectConstants {

    public static final String WEBSOCKET_URL = "/open-platform/websocket";

    public static final String SUB_PROTOCOLS = "WebSocket";

    public static final Boolean ALLOW_EXTENSIONS = false;

    public static final Integer MAX_FRAME_SIZE = 65536;

    public static final Integer MAX_CONTENT_LENGTH = 65536;

    public static final Integer THREAD_WAIT_NUM = 128;

    public static final Integer READER_IDLE_TIME = 2;

    public static final Integer WRITER_IDLE_TIME = 2;

    public static final Integer ALL_IDLE_TIME = 2;

    public static final String SEC_WEBSOCKET_PROTOCOLS = "Sec-WebSocket-Protocol";

    public static final String RECEIVE_MESSAGE_TYPE = "messageType";

    public static final String RECEIVE_MESSAGE = "message";

    public static final String WEBSOCKET_BROADCAST = "websocket-broadcast";

    public static final String OFFLINE_MESSAGE_BY_USER = "OFFLINE_MESSAGE_BY_USER:";

    public static final String WEBSOCKET_PRIVATE = "websocket-private";

    public static final String WS = "ws";

    public static final String WSS = "wss";
}
