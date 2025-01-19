package com.zhouzhou.cloud.payservice.constants;

public class WxConstants {

    /**
     * 主商户appId
     */
    public static final String WX_SP_APP_ID = "sp_app_id";

    /**
     * 主商户mchId
     */
    public static final String WX_SP_MCH_ID = "sp_mch_id";

    /**
     * 子商户mchId
     */
    public static final String WX_SUB_MCH_ID = "sub_mch_id";

    /**
     * 子商户appId
     */
    public static final String WX_SUB_APP_ID = "sub_app_id";

    /**
     * 商户私钥
     */
    public static final String WX_MERCHANT_KEY = "merchant_private_key";

    /**
     * 商户证书序列号
     */
    public static final String WX_SERIAL_NO = "serial_no";


    /**
     * API V3密钥（用于签名和加密）
     */
    public static final String WX_API_V3_KEY = "api_v3_key";


    /**
     * 支付接口URL（用于发起微信支付请求）
     */
    public static final String WX_PAY_URL = "pay_url";

    /**
     * 支付状态查询接口URL（用于查询微信支付状态）
     */
    public static final String WX_ORDER_QUERY_URL = "order_query_url";

    /**
     * 微信小程序密钥
     */
    public static final String WX_APPLET_SECRET = "applet_secret";

    /**
     * AccessToken 缓存key
     */
    public static final String ZHOUZHOU_PLATFORM_WX_APPLET_ACCESS_TOKEN = "WX_APPLET_ACCESS_TOKEN";

    /**
     * 订单描述
     */
    public static final String ORDER_DESCRIPTION = "description";

    /**
     * 每日橙订单编号
     */
    public static final String OUT_TRADE_NO = "out_trade_no";

    /**
     * 微信支付回调每日橙接收URL
     */
    public static final String WX_PAY_NOTIFY_URL = "notify_url";

    public static final String WX_PAY_NOTIFY_REFUND_URL = "refund_notify_url";

    /**
     * 微信支付金额
     */
    public static final String WX_PAY_AMOUNT = "amount";


    /**
     * 微信支付者信息
     */
    public static final String WX_PAY_PAYER = "payer";


    /**
     * 退款接口URL
     */
    public static final String WX_REFUND_URL = "refund_url";

    /**
     * 退款请求状态url
     */
    public static final String WX_REFUND_QUERY_URL = "refund_query_url";

    /**
     * 撤销订单接口URL
     */
    public static final String WX_CANCEL_ORDER_URL ="cancel_order_url" ;

    /**
     * 商户私钥
     */
    public static final String WX_MERCHANT_PRIVATE_KEY = "merchant_private_key";

    /**
     * 交易单号
     */
    public static final String TRANSACTION_ID = "transaction_id";

    /**
     * 交易状态
     */
    public static final String TRADE_STATE = "trade_state";

    /**
     * 退款状态
     */
    public static final String REFOUND_STATUS = "refund_status";

    /**
     * 预支付ID
     */
    public static final String WX_PREPAY_ID = "prepay_id=";


    /**
     * 商城订单缓存key
     */
    public static final String ZHOUZHOU_ORDER_KEY = "zhouzhou_order_key";
}
