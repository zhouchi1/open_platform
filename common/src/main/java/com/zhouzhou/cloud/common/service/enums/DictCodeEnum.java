package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ：sunqingrui
 */
@Getter
@AllArgsConstructor
public enum DictCodeEnum {

    /**
     * 仓库库区属性
     */
    AREA_ATTRIBUTE("area_attribute", "仓库库区属性"),
    /**
     * 仓库属性
     */
    STOCK_ATTRIBUTE("stock_attribute", "仓库属性"),
    /**
     * 商品保质期类型
     */
    EXPIRE_TYPE("expire_type", "商品保质期类型"),

    /**
     * 用于配置第三方连接url
     */
    OPEN_API("open_api", "用于配置第三方连接url"),

    /**
     * 审批流程模板
     */
    PROCESS_CODE("process_code", "审批流程模板"),

    /**
     * 回调配置
     */
    FLOW_CALL_BACK("flow_call_back", "回调配置"),

    /**
     * 审核状态
     */
    OSS_CONFIG("oss_config", "OSS配置"),
    /**
     * 级别枚举
     */
    LEVEL_CODE("level_code", "级别枚举"),
    /**
     * 商品出库类型
     */
    PRODUCT_OUT_TYPE("product_out_type", "级别枚举"),
    /**
     * 商品属性枚举
     */
    PRODUCT_PROPERTY("product_property", "商品属性枚举"),
    /**
     * 商品类型枚举
     */
    PRODUCT_TYPE("product_type", "商品类型枚举"),
    /**
     * 一品多码 类型
     */
    PRODUCT_UPC_TYPE("product_upc_type", "一品多码 类型"),
    /**
     * 供应商类型
     */
    SUPPLIER_TYPE("supplier_type", "供应商类型"),

    /**
     * webSocket openApi->manager
     */
    WEBSOCKET_SEND_IP("webSocket_send_ip", "webSocket转发ip"),


    // -------------------------------------微信配置信息Begin---------------------------------------

    WX_CONFIG("wx_config", "微信配置信息"),

    WX_SP_APP_ID("sp_appid","微信小程序id"),

    WX_SP_MCH_ID("sp_mchid","主商户id"),

    WX_APPLET_SECRET("applet_secret","微信小程序密钥"),

    WX_SUB_MCH_ID("sub_mchid","子商户id"),

    WX_SUB_APP_ID("sub_appid","子商户appId"),

    WX_PAY_URL("pay_url","微信支付接口URL（用于发起微信支付请求）"),

    WX_ORDER_QUERY_URL("order_query_url","支付订单查询接口URL（用于查询订单支付状态）"),

    WX_REFUND_URL("refund_url","退款接口URL（用于发起退款请求）"),

    WX_REFUND_QUERY_URL("refund_query_url","退款查询接口URL（用于查询退款状态）"),

    WX_CANCEL_ORDER_URL("cancel_order_url","取消订单接口URL（用于取消未支付的订单）"),

    WX_API_V3_KEY("api_v3_key","API V3密钥（用于签名和加密）"),

    WX_MERCHANT_PRIVATE_KEY("merchant_private_key","商户私钥"),

    WX_SERIAL_NO("serial_no","证书序列号"),

    WX_NOTIFY_URL("notify_url","支付结果通知回调URL（用于接收支付结果通知）"),

    WX_REFUND_NOTIFY_URL("refund_notify_url","退款结果通知回调URL（用于接收退款结果通知）"),

    // -------------------------------------微信配置信息End---------------------------------------

    // -------------------------------------短信配置信息Begin---------------------------------------

    VERIFY_CODE_TEMPLATE_ID("code_template_id","短信验证码模板ID"),

    SMS_CONFIG("sms_config","短信配置信息"),

    VERIFY_ACCOUNT("account","获取验证码的账户获取验证码的账户"),

    VERIFY_TRADE_KEY("trade_key","获取验证码的密钥"),

    VERIFY_URL("verify_url","获取验证码的URL地址"),

    REGULAR_EXPRESSION_CONFIG("regular_expression_config","正则表达式配置"),

    USER_NAME_MATCH("user_name_match","用户名正则匹配"),

    USER_PHONE_MATCH("user_phone_match","用户手机号正则匹配"),

    USER_PASSWORD_MATCH("user_password_match","用户密码匹配"),

    // -------------------------------------短信配置信息End---------------------------------------


    // -------------------------------------商品首页展示权重配置信息Begin---------------------------------------

    PRODUCT_DISPLAY_WEIGHT("homepage_display_rule_weight","商品首页展示权重"),

    PRODUCT_DISPLAY_CART_WEIGHT("product_display_cart_weight","商品首页购物车负责展示权重"),

    PRODUCT_DISPLAY_BROWSE_WEIGHT("product_display_browse_weight","商品首页浏览记录负责展示权重"),

    PRODUCT_DISPLAY_COLLECT_WEIGHT("product_display_collect_weight","商品首页收藏结果负责展示权重"),

    PRODUCT_DISPLAY_PURCHASE_WEIGHT("product_display_purchase_weight","商品首页下单结果负责展示权重"),

    PRODUCT_DISPLAY_COLLABORATIVE_FILTERING_WEIGHT("product_display_collaborative_filtering_weight","商品协同过滤算法结果负责展示过滤权重"),

    COLLABORATIVE_FILTER_CONFIG("collaborative_filter_config","协同过滤商品推荐模型文件"),

    COLLABORATIVE_FILTER_MODEL_OSS_URL("collaborative_filter_model_oss_url","协同过滤OSS模型文件"),

    // -------------------------------------商品首页展示权重配置信息End----------

    ;

    private final String key;
    private final String desc;

}
