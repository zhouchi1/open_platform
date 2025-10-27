package com.zhouzhou.cloud.payservice.request.pay.wxpay;

import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayReFoundAmountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付退款接口
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayReFoundReq implements Serializable {

    private static final Long serialVersionUID = 6742378462387462738L;

    // --------------------------------必填项目开始---------------------------------------//

    @Schema(name = "商户Id")
    private String sub_mchid;

    @Schema(name = "商城退款单号")
    private String out_refund_no;

    @Schema(name = "退款金额")
    private WxPayReFoundAmountDTO amount;

    // --------------------------------必填项目结束---------------------------------------//

    // --------------------------------选填项目开始---------------------------------------//

    @Schema(name = "微信支付订单号----与商城单号二选一必传")
    private String transaction_id;

//    @Schema(name = "商城订单号----与微信支付单号二选一必传")
//    private String out_trade_no;

    @Schema(name = "退款原因")
    private String reason;
//
    @Schema(name = "退款通知url")
    private String notify_url;

//    @Schema(name = "退款资金来源")
//    private String funds_account;

//    @Schema(name = "退款商品信息-需要指定商品进行退款时 必传！！！")
//    private String goods_detail;

    // --------------------------------选填项目结束---------------------------------------//

}
