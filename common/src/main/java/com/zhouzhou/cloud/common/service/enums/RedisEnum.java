package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.MessageFormat;

/**
 * @author ：林雾
 * @date ：2020/07/03
 * @description :
 */
@Getter
@AllArgsConstructor
public enum RedisEnum {
  /**
   * 访问token
   */
  ACCESS_TOKEN("daily_orange:access_token:{0}", null, "访问token"),
  /**
   * 刷新token
   */
  REFRESH_TOKEN("daily_orange::refresh_token:{0}", null, "刷新 token"),

  /**
   * 注册验证码
   */
  REGISTER_MOBILE_CODE("daily_orange::register_mobile_code:{0}",null,"注册验证码"),

  /**
   * 注册验证码最后发送时间
   */
  REGISTER_LAST_SEND_CODE_TIME("daily_orange::register_last_send_code_time:{0}",null,"注册验证码最后发送时间"),


  /**
   * 登录验证码
   */
  LOGIN_MOBILE_CODE("daily_orange::login_mobile_code:{0}",null,"登录验证码"),

  /**
   * 登录验证码最后发送时间
   */
  LOGIN_LAST_SEND_CODE_TIME("daily_orange::login_last_send_code_time:{0}",null,"登录验证码"),

  /**
   * 登录token
   */
  SIGN_IN_TOKEN("orange_wms::sign_in_token:{0}:{1}", null, "刷新 token"),

  DING_APP_TOKEN("orange_wms::access_token:{0}", null, "访问token"),
  MQ_MESSAGE_ID("orange_wms::mq_message_id:{0}", null, "mq消息ID"),

  CONFIRM_RECEIVED_PRODUCT("orange_wms::confirm_received_product:{0}", null, "确认收货商品redis key"),

  STORE_RETURN_SHEET_PRODUCT("orange_wms::store_return_sheet_product:{0}", null, "门店退货单商品redis key"),

  JOB_TOKEN("orange_wms:job_token:{0}", null, "job锁"),
  OUT_BOUND_SHEET_TOKEN("orange_wms:out_bound_sheet_token:{0}", null, "新增出库单锁"),
  PICK_SHEET_TOKEN("orange_wms:pick_sheet_token:{0}", null, "新增拣货单锁"),
  BOX_ORDER_SHEET_TOKEN("orange_wms:box_order_sheet_token:{0}", null, "物流箱登记记录锁定"),



  ;

  private final String key;
  private final Long expire;
  private final String desc;

  public String createKey(Object... obj) {
    return MessageFormat.format(this.getKey(), obj);
  }
}
