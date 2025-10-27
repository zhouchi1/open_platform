package com.zhouzhou.cloud.websocketservice.constant;

import java.time.format.DateTimeFormatter;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-09-02
 * @Description: WebSocket常量类
 */
public class ConnectConstants {

    private ConnectConstants() {
    }

    public static final String WEBSOCKET_URL = "/open—platform/websocket";

    public static final String SUB_PROTOCOLS = "WebSocket";

    public static final Boolean ALLOW_EXTENSIONS = false;

    public static final Integer MAX_FRAME_SIZE = 65536;

    public static final Integer MAX_CONTENT_LENGTH = 65536;

    public static final Integer THREAD_WAIT_NUM = 128;

    public static final Integer READER_IDLE_TIME = 2;

    public static final Integer WRITER_IDLE_TIME = 2;

    public static final Integer ALL_IDLE_TIME = 2;

    public static final String SEC_WEBSOCKET_PROTOCOLS = "Sec-WebSocket-Protocol";

    public static final String DEVICE_TYPE = "Device-Type";

    public static final String RECEIVE_MESSAGE_TYPE = "messageType";

    public static final String RECEIVE_MESSAGE = "message";

    public static final String WEBSOCKET_MESSAGE = "websocket-message";

    public static final String OFFLINE_MESSAGE_BY_USER = "OFFLINE_MESSAGE_BY_USER:";

    public static final String OFFLINE_MESSAGE_BY_USER_EXPIRE = "OFFLINE_MESSAGE_BY_USER_EXPIRE";

    public static final String WS_NODE_STATUS = "ws-node-status:";

    public static final String NODE_CHANNEL_USER_INFO = "node_channel_user_info:";

    public static final String SOCKET_MESSAGE_ID = "SOCKET_MESSAGE_ID";

    public static final String WS = "ws";

    public static final String WSS = "wss";

    /**
     * 商城PC端
     */
    public static final String SHOPPING_PC = "SHOPPING_PC";

    /**
     * 商城小程序端
     */
    public static final String SHOPPING_APPLET = "SHOPPING_APPLET";


    public static final String REDIS_CLEAN_ALL_CONNECT_KEY = "REDIS_CLEAN_ALL_CONNECT_KEY";

    public static final String REDIS_CLEAN_ALL_UNLESS_MESSAGE_KEY = "REDIS_CLEAN_ALL_UNLESS_MESSAGE_KEY";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
