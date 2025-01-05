package com.zhouzhou.cloud.common.service.excepetions;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: sunqr
 * @Date: 2022/10/12 9:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {
  private IErrorCode bizCode;
  private String msg;

  public BizException(String msg) {
    super(msg);
    this.msg = msg;
    this.bizCode = BizExCode.BUSINESS_ERROR;
  }

  public BizException(IErrorCode errorCode) {
    super(errorCode.getDesc());
    this.bizCode = errorCode;
    this.msg = errorCode.getDesc();
  }

  public BizException(IErrorCode errorCode, String msg) {
    super(msg);
    this.bizCode = errorCode;
    this.msg = msg;
  }
}
