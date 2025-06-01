package com.zhouzhou.cloud.payservice.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zhouzhou.cloud.common.req.BaseStringReq;
import com.zhouzhou.cloud.common.service.base.ResponseData;
import com.zhouzhou.cloud.common.utils.ResponseDataUtil;
import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayReFoundStatusDTO;
import com.zhouzhou.cloud.payservice.dto.wxpay.WxPayStatusDTO;
import com.zhouzhou.cloud.payservice.req.wxpay.WxPayCallBackReq;
import com.zhouzhou.cloud.payservice.resp.wxpay.WxPayCallBackResp;
import com.zhouzhou.cloud.payservice.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-29
 * @Description: 微信支付
 */
@RestController
@RequestMapping("/wxPay")
@Api(tags = "商城小程序 >> 微信支付")
public class WxPayController {

    @Resource
    private WxPayService wxPayService;

    @ApiOperation("微信支付状态查询")
    @ApiOperationSupport(author = "Sr.Zhou")
    @PostMapping("/wxPayStatusQuery")
    ResponseData<WxPayStatusDTO> wxPayStatusQuery(@RequestBody BaseStringReq baseStringReq){
        return ResponseDataUtil.success(wxPayService.wxPayStatusQuery(baseStringReq.getStr()));
    }

    @ApiOperation("微信退款状态查询")
    @ApiOperationSupport(author = "Sr.Zhou")
    @PostMapping("/wxPayReFoundStatusQuery")
    ResponseData<WxPayReFoundStatusDTO> wxPayReFoundStatusQuery(@RequestBody BaseStringReq baseStringReq){
        return ResponseDataUtil.success(wxPayService.wxPayReFoundStatusQuery(baseStringReq.getStr()));
    }

    @ApiOperation("微信支付回调")
    @ApiOperationSupport(author = "Sr.Zhou")
    @PostMapping("/wxPayCallback")
    ResponseData<WxPayCallBackResp> wxPayCallback(@RequestBody WxPayCallBackReq wxPayCallBackReq){
        return ResponseDataUtil.success(wxPayService.wxPayCallback(wxPayCallBackReq));
    }

    @ApiOperation("微信退款回调")
    @ApiOperationSupport(author = "Sr.Zhou")
    @PostMapping("/wxPayReFoundCallback")
    ResponseData<WxPayCallBackResp> wxPayReFoundCallback(@RequestBody WxPayCallBackReq wxPayCallBackReq){
        return ResponseDataUtil.success(wxPayService.wxPayReFoundCallback(wxPayCallBackReq));
    }
}
