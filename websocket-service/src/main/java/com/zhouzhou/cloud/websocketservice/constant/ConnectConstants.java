package com.zhouzhou.cloud.websocketservice.constant;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-09-02
 * @Description: WebSocket常量类
 */
public class ConnectConstants {
    /**
     * WebSocket相关参数配置
     */
    public static final String WEBSOCKET_URL = "/open-platform/websocket";

    public static final Integer WEBSOCKET_PORT = 5112;

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

    public static final String WEBSOCKET_PRIVATE = "websocket-private";

    public static final String USER = "user:";

    public static final String CHANNEL_ID = "channelId:";

    public static final String WS_NODE_STATUS = "ws-node-status:";

    public static final String NODE_ID = "nodeId:";

    public static final String WS = "ws";

    public static final String WSS = "wss";
}
