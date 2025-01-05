package com.zhouzhou.cloud.common.service.enums;

/**
 * @Author: sunqr
 * @Date: 2022/10/12 17:26
 */
public enum ResponseEnum {
    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    UNAUTHORIZED(401, "身份未认证"),
    ILLEGAL_AUTHORIZE(403, "暂无操作权限"),
    PHONE_NUMBER_ERROR(202, "手机号已注册"),
    THE_INTERVAL_OF_DATA_IS_TOO_LONG(202, "限制查询7天间隔内的数据"),
    TOKEN_TIMEOUT(1001, "授权失效"),
    TOKEN_INVALID(1002, "授权失败"),
    INVALID_PARAMS(9001, "参数有误"),
    MISSING_PARAMETER(9002, "缺少必要参数"),
    INVALID_RSA_KEY(9003, "超时失效"),
    SIGN_ERROR(9004, "签名错误"),
    DATA_ALREADY_EXISTS(9005, "数据已存在"),
    INVALID_STATUS(9006, "状态不符"),
    DELETE_THE_FAILURE(9008, "数据删除失败"),
    LOGIN_EXPIRED(9006, "登录已过期，请重新登录"),
    REQUEST_METHOD_NOT_SUPPORTED(9998, "请求方法不受支持"),
    ERROR(9999, "系统异常"),
    NOTIFY_FAIL(80181, "回调通知失败"),
    /**
     * pipeline
     */
    CONTEXT_IS_NULL(9801, "流程上下文为空"),
    PROCESS_TEMPLATE_IS_NULL(9802, "流程模板配置为空"),
    PROCESS_LIST_IS_NULL(9803, "业务处理器配置为空"),
    PROCESS_MODEL_IS_NULL(9804, "处理模型为空"),
    NEED_BREAK_IS_NULL(9805, "请传入是否中断标识"),
    /**
     * 业务错误
     */
    BUSINESS_ERROR(1003, "业务错误"),
    ;

    private final Integer code;
    private final String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}

