package com.zhouzhou.cloud.payservice.dto.wxpay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付预支付提交订单信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayOrderDTO implements Serializable {

    private static final long serialVersionUID = 6873648726384762312L;

    // --------------------------------------------必填项开始---------------------------------------------------//

    @Schema(name = "服务商应用ID")
    private String sp_appid;

    @Schema(name = "服务商户号")
    private String sp_mchid;

    @Schema(name = "子商户号/二级商户号")
    private String sub_mchid;

    @Schema(name = "订单描述")
    private String description;

    @Schema(name = "商城订单编号")
    private String out_trade_no;

    @Schema(name = "微信支付-商城接收微信回调开发接口")
    private String notify_url;

    @Schema(name = "微信支付-金额信息")
    private WxPayAmountDTO amount;

    @Schema(name = "微信支付-支付者信息")
    private WxPayPayerDTO payer;

    // ---------------------------------------------必填项结束--------------------------------------------------//

    // ---------------------------------------------选填项开始--------------------------------------------------//

    @Schema(name = "微信支付-是否需要开票")
    private boolean support_fapiao;

    @Schema(name = "子商户/二级商户应用ID - 注意！！！ 如果必填项中payer中填写了sp_sub_openid 则该项为必填项")
    private String sub_appid;

    @Schema(name = "微信支付-订单优惠详情信息(包含商品的信息)")
    private WxPayPromotionDTO detail;
//
//    @Schema(name = "微信支付-附加数据")
//    private String attach;
//
//    @Schema(name = "微信支付-订单优惠标记")
//    private String goods_tag;
//
//    @Schema(name = "微信支付-结算信息")
//    private WxPaySettleInfoDTO settle_info;
//
//    @Schema(name = "微信支付-交易结束时间")
//    private String time_expire;
//
//    @Schema(name = "微信支付-支付场景信息")
//    private WxPaySceneInfoDTO scene_info;

    // ---------------------------------------------选填项结束--------------------------------------------------//
}
