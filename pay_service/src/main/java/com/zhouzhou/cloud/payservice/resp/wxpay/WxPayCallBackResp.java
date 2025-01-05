package com.zhouzhou.cloud.payservice.resp.wxpay;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付回调响应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WxPayCallBackResp extends BaseAMO {

    /**
     * 返回状态码
     */
    private String code;

    /**
     * 返回信息
     */
    private String message;
}
