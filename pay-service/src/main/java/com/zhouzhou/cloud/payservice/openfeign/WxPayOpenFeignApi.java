package com.zhouzhou.cloud.payservice.openfeign;

import com.zhouzhou.cloud.payservice.config.WxPayOpenFeignConfig;
import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayOrderDTO;
import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayReFoundStatusDTO;
import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayStatusDTO;
import com.zhouzhou.cloud.payservice.req.wxpay.WxPayReFoundReq;
import com.zhouzhou.cloud.payservice.resp.wxpay.WxPayPrePayInformationResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-27
 * @Description: 微信支付接口 - 调取支付模块接口
 */
@FeignClient(name = "wxPay-service", url = "https://api.mch.weixin.qq.com", configuration = WxPayOpenFeignConfig.class)
public interface WxPayOpenFeignApi {

    /**
     * 发起微信预支付请求
     * @param wxPayOrderDTO 微信支付请求参数
     */
    @PostMapping("/v3/pay/partner/transactions/jsapi")
    ResponseEntity<WxPayPrePayInformationResp> prePayToWeiXin(@RequestBody WxPayOrderDTO wxPayOrderDTO);

    /**
     * 微信退款请求
     * @param wxPayReFoundReq 微信退款请求参数
     */
    @PostMapping("/v3/refund/domestic/refunds")
    String wxPayReFound(@RequestBody WxPayReFoundReq wxPayReFoundReq);

    /**
     * 微信支付查询订单状态
     * @param transaction_id 微信订单号
     * @param sp_mchid       商户号
     * @param sub_mchid      子商户号
     * @return 订单信息
     */
    @GetMapping("/v3/pay/partner/transactions/id/{transaction_id}?sp_mchid={sp_mchid}&sub_mchid={sub_mchid}")
    WxPayStatusDTO wxPayStatusQuery(@PathVariable("transaction_id") String transaction_id, @PathVariable("sp_mchid") String sp_mchid, @PathVariable("sub_mchid") String sub_mchid);

    /**
     * 微信退款查询
     * @param out_refund_no 商城退款单号
     * @param sub_mchid     子商户号
     * @return 微信退款返回结果
     */
    @GetMapping("/v3/refund/domestic/refunds/{out_refund_no}?sub_mchid={sub_mchid}")
    WxPayReFoundStatusDTO wxPayReFoundStatusQuery(@PathVariable("out_refund_no") String out_refund_no, @PathVariable("sub_mchid") String sub_mchid);
}
