package com.zhouzhou.cloud.payservice.request.pay.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款回调请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayCallBackReq {

    /**
     * 通知唯一ID
     */
    private String id;

    /**
     * 通知创建时间
     */
    private String create_time;

    /**
     * 资源类型
     */
    private String resource_type;

    /**
     * 通知类型
     */
    private String event_type;

    /**
     * 回调摘要
     */
    private String summary;

    /**
     * 通知资源数据
     */
    private WxPayCallBackResourceReq resource;
}
