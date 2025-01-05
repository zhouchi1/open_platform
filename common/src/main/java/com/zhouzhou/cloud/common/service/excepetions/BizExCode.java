package com.zhouzhou.cloud.common.service.excepetions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: sunqr
 * @Date: 2022/10/12 9:56
 */

@AllArgsConstructor
@Getter
public enum BizExCode implements IErrorCode {
  /**
   * 成功
   */
  SUCCESS(200, "成功"),
  /**
   * 数据错误
   */
  DATA_ERROR(201, "数据错误"),

  /**
   * 无权访问
   */
  PERMISSION_DENIED(403, "无权访问"),
  /**
   * 服务调用异常
   */
  INVOKE_SERVICE_ERROR(1001, "服务调用异常"),
  /**
   * 用户未登录
   */
  NOT_LOGIN(1002, "用户未登录"),
  /**
   * 业务错误
   */
  BUSINESS_ERROR(1003, "业务错误"),

  /**
   * 白名单未配置
   */
  PERMISSION_DENIED_NOTIN_WHITE(1042, "无权访问:白名单未配置"),
  /**
   * 黑名单已配置
   */
  PERMISSION_DENIED_IN_BLACK(1043, "无权访问:黑名单已配置"),
  /**
   * 参数有误
   */
  PARAM_ERROR(1005, "参数有误"),
  /**
   * 登录过期
   */
  LOGIN_EXPIRED(1006, "登录过期"),
  /**
   * 用户名不存在
   */
  USERNAME_NON_EXISTENT(1007, "用户名不存在"),
  /**
   * 用户名已存在
   */
  USERNAME_ALREADY_EXISTED(1008, "用户名已存在"),
  /**
   * 非法参数
   */
  INVALID_PARAMS(1013, "非法参数"),
  /**
   * 已存在的数据
   */
  CHECK_ERROR(1201, "已存在的数据"),
  /**
   * 非法参数
   */
  NO_BIND_ERROR(1202, "用户钉钉未绑定"),
  /**
   * 9999异常
   */
  UNKNOWN(9999, "内部搞了点儿事情，请联系一下管理员处理"),
  /**
   * 无角色权限
   */
  UN_PERMISSION(1403, "无角色权限"),

  /**
   * 请求太频繁
   */
  REQUEST_TOO_FAST(1106,"小橙处理不过来啦~"),

  /**
   * 当前用户未绑定租户，请先绑定租户！
   */
  NOT_BIND_TENANT(996,"当前用户未绑定租户，请先绑定租户！"),
  ;

  private final Integer code;

  private final String desc;


}
