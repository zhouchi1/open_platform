package com.zhouzhou.cloud.common.service.excepetions;

import java.io.Serializable;

/**
 * @Author: sunqr
 * @Date: 2022/10/12 9:56
 */
public interface IErrorCode<C extends Serializable> {
    /**
     * code编码
     * @return C
     */
    C getCode();

    /**
     * code详情
     * @return String
     */
    String getDesc();
}
