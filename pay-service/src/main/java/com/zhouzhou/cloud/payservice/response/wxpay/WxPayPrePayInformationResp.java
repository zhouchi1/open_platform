package com.zhouzhou.cloud.payservice.response.wxpay;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 预支付返回响应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WxPayPrePayInformationResp extends BaseAMO {

    private static final long serialVersionUID = 3864578236458726381L;

    @Schema(name = "预支付交易会话标识-有效期2小时")
    private String prepay_id;

    @Schema(name = "随机字符串")
    private String nonceStr;

    @Schema(name = "时间戳")
    private String timeStamp;

    @Schema(name = "签名")
    private String paySign;
}
