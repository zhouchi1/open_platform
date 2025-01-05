package com.zhouzhou.cloud.payservice.dto.wxpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付订单-resource解密反序列化
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayCallBackDecryptDTO implements Serializable {

    private static final long serialVersionUID = 2348762378462178642L;

    private String transaction_id;

    private WxPayCallBackDecryptAmountDTO amount;

    private String mchid;

    private String trade_state;

    private String bank_type;

    private List<WxPayCallBackDecryptPromotionDetailDTO> promotion_detail;

    private String success_time;

    private WxPayCallBackDecryptPayerDTO payer;

    private String out_trade_no;

    private String AppID;

    private String trade_state_desc;

    private String trade_type;

    private String attach;

    private WxPayCallBackDecryptSceneInfoDTO scene_info;
}
