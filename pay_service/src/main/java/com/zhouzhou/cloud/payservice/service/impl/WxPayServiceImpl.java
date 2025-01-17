package com.zhouzhou.cloud.payservice.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.zhouzhou.cloud.common.dubboresp.OrderInfoResp;
import com.zhouzhou.cloud.common.entity.ShopOrderDetails;
import com.zhouzhou.cloud.common.utils.RabbitMqSender;
import com.zhouzhou.cloud.common.utils.RedisUtil;
import com.zhouzhou.cloud.payservice.dto.wxpay.*;
import com.zhouzhou.cloud.payservice.enums.PayCurrencyEnum;
import com.zhouzhou.cloud.payservice.enums.WxPayTradeStateEnum;
import com.zhouzhou.cloud.payservice.openfeign.WxPayOpenFeignApi;
import com.zhouzhou.cloud.payservice.req.wxpay.WxPayCallBackReq;
import com.zhouzhou.cloud.payservice.req.wxpay.WxPayReFoundReq;
import com.zhouzhou.cloud.payservice.resp.wxpay.WxPayCallBackResp;
import com.zhouzhou.cloud.payservice.resp.wxpay.WxPayPrePayInformationResp;
import com.zhouzhou.cloud.payservice.service.WxPayService;
import com.zhouzhou.cloud.payservice.utils.WxUtil;
import com.zhouzhou.cloud.common.service.interfaces.WxPayOrderServiceApi;
import com.zhouzhou.cloud.common.utils.BizExUtil;
import com.zhouzhou.cloud.common.utils.LoginUserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

import java.math.BigDecimal;

import static com.zhouzhou.cloud.payservice.constants.WxConstants.*;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-12-03
 * @Description: 微信支付服务实现层
 */
@Slf4j
@Service
@RefreshScope
public class WxPayServiceImpl implements WxPayService {

    // version：调用的微服务版本号
    // loadbalance：负载均衡策略 1、random：随机；2、roundrobin：轮训；3、consistentHash：一致性哈希；4、leastActive：最小活跃度
    @Reference(version = "1.0.0", loadbalance = "leastActive")
    private WxPayOrderServiceApi wxPayOrderServiceApi;

    @Resource
    private WxUtil wxUtil;

    @Resource
    private RabbitMqSender rabbitMQSender;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private WxPayOpenFeignApi wxPayOpenFeignApi;

    @Value("${warehouse.exchangeName}")
    private String exchangeName;

    @Value("${warehouse.routeKey}")
    private String routeKey;


    @Override
    public WxPayPrePayInformationResp wxPayPreRequest(WxPayOrderDTO wxPayOrderDTO) {

        // 一、微信支付相关配置信息
        WxPayConfigDTO wxPayConfigDTO = wxUtil.wxPayConfigQuery();

        // 二、组装参数用于微信预支付传递参数
        assembleWxReqParameter(wxPayOrderDTO, wxPayConfigDTO);

        // 三、openFeign调用微信支付 换回prePayId
        ResponseEntity<WxPayPrePayInformationResp> wxPayPrePayResult = wxPayOpenFeignApi.prePayToWeiXin(wxPayOrderDTO);
        BizExUtil.requirefalse(!wxPayPrePayResult.getStatusCode().is2xxSuccessful(), "网络异常，请稍后再试！");

        BizExUtil.requirefalse(ObjectUtils.isEmpty(wxPayPrePayResult.getBody()), "1-微信支付失败，请联系技术人员！");
        BizExUtil.requirefalse(ObjectUtils.isEmpty(wxPayPrePayResult.getBody().getPrepay_id()), "2-微信支付失败，请联系技术人员！");

        // 四、组装参数获取数字签名
        String nonceStr = RandomUtil.randomString(32);
        String prepay_id = WX_PREPAY_ID + wxPayPrePayResult.getBody().getPrepay_id();
        String timeStamp = String.valueOf(System.currentTimeMillis());

        // 五、添加数字签名 并返回
        WxPayPrePayInformationResp wxPayPrePayInformationResp = wxUtil.wxSign(wxPayConfigDTO.getSub_app_id(), timeStamp
                , nonceStr, prepay_id, wxPayConfigDTO.getMerchant_private_key());
        BizExUtil.requirefalse(ObjectUtils.isEmpty(wxPayPrePayInformationResp), "3-微信支付失败,请联系技术人员！");

        // 六、微信支付返回值 前端唤起微信支付
        return wxPayPrePayInformationResp;
    }

    @Override
    public WxPayStatusDTO wxPayStatusQuery(String str) {
        WxPayConfigDTO wxPayConfigDTO = wxUtil.wxPayConfigQuery();
        // 【OpenFeign调用微信支付状态查询服务】
        return wxPayOpenFeignApi.wxPayStatusQuery(str, wxPayConfigDTO.getSp_mch_id(), wxPayConfigDTO.getSub_mch_id());
    }

    /**
     * 查询微信退款状态
     *
     * @param str 商城订单号
     * @return 退款状态
     */
    @Override
    public WxPayReFoundStatusDTO wxPayReFoundStatusQuery(String str) {
        WxPayConfigDTO wxPayConfigDTO = wxUtil.wxPayConfigQuery();
        // 【OpenFeign调用微信退款状态查询服务】
        return wxPayOpenFeignApi.wxPayReFoundStatusQuery(str, wxPayConfigDTO.getSub_mch_id());
    }

    @Override
    public WxPayCallBackResp wxPayCallback(WxPayCallBackReq wxPayCallBackReq) {

        // 微信支付相关配置信息
        WxPayConfigDTO wxPayConfigDTO = wxUtil.wxPayConfigQuery();

        try {
            // 获取加密数据
            String ciphertext = wxPayCallBackReq.getResource().getCiphertext();
            String associatedData = wxPayCallBackReq.getResource().getAssociated_data();
            String nonce = wxPayCallBackReq.getResource().getNonce();

            // 解密加密数据，使用AES-GCM解密
            String decryptedData = wxUtil.decrypt(wxPayConfigDTO.getApi_v3_key(), ciphertext, associatedData, nonce);
            WxPayCallBackDecryptDTO wxPayCallBackDecryptDTO = JSON.parseObject(decryptedData, WxPayCallBackDecryptDTO.class);

            log.info("微信支付回调解密数据：{}", decryptedData);

            // 处理解密后的数据
            String transactionId = wxPayCallBackDecryptDTO.getTransaction_id(); // 微信支付订单号
            String outTradeNo = wxPayCallBackDecryptDTO.getOut_trade_no();      // 商城订单号
            String tradeState = wxPayCallBackDecryptDTO.getTrade_state();       // 支付状态

            // 根据支付结果处理商户业务逻辑 【Dubbo调用订单服务】
            if (WxPayTradeStateEnum.SUCCESS.getCode().equals(tradeState)) {
                wxPayCallBackHandle(outTradeNo, transactionId, wxPayConfigDTO);

                // 【RabbitMQ发送消息到仓库服务】扣减库存
                rabbitMQSender.sendRoutingMessage(exchangeName, routeKey, outTradeNo);
            }

            // 返回处理结果，通知微信支付停止回调
            return new WxPayCallBackResp(WxPayTradeStateEnum.SUCCESS.getCode(), WxPayTradeStateEnum.SUCCESS.getDesc());

        } catch (Exception e) {
            log.error("微信支付回调通知处理失败：{}", e.getMessage());

            // 如果处理失败，返回错误信息
            return new WxPayCallBackResp(WxPayTradeStateEnum.PAYERROR.getCode(), WxPayTradeStateEnum.PAYERROR.getDesc());
        }
    }

    @Override
    public WxPayCallBackResp wxPayReFoundCallback(WxPayCallBackReq wxPayCallBackReq) {

        // 微信支付相关配置信息
        WxPayConfigDTO wxPayConfigDTO = wxUtil.wxPayConfigQuery();

        try {
            // 获取加密数据
            String ciphertext = wxPayCallBackReq.getResource().getCiphertext();
            String associatedData = wxPayCallBackReq.getResource().getAssociated_data();
            String nonce = wxPayCallBackReq.getResource().getNonce();

            // 解密加密数据，使用AES-GCM解密
            String decryptedData = wxUtil.decrypt(wxPayConfigDTO.getApi_v3_key(), ciphertext, associatedData, nonce);

            // 处理解密后的数据
            WxPayReFoundCallBackDecryptDTO wxPayReFoundCallBackDecryptDTO = JSON.parseObject(decryptedData, WxPayReFoundCallBackDecryptDTO.class);

            log.info("微信支付退款回调解密数据：{}", decryptedData);

            String transactionId = wxPayReFoundCallBackDecryptDTO.getTransaction_id(); // 微信支付订单号
            String reFoundStatus = wxPayReFoundCallBackDecryptDTO.getRefund_status();  // 支付状态

            // 根据支付结果处理商户业务逻辑
            if (WxPayTradeStateEnum.SUCCESS.getCode().equals(reFoundStatus)) {
                // 退款成功的逻辑，更新订单状态 【Dubbo调用订单服务】
                wxPayReFoundCallBackHandle(transactionId);
            }

            // 返回处理结果，通知微信支付停止回调
            return new WxPayCallBackResp(WxPayTradeStateEnum.SUCCESS.getCode(), WxPayTradeStateEnum.SUCCESS.getDesc());
        } catch (Exception e) {
            log.error("微信支付退款回调通知处理失败：{}", e.getMessage());
            // 如果处理失败，返回错误信息
            return new WxPayCallBackResp(WxPayTradeStateEnum.PAYERROR.getCode(), WxPayTradeStateEnum.PAYERROR.getDesc());
        }
    }

    /**
     * 处理微信支付成功的逻辑
     *
     * @param outTradeNo    商户订单号
     * @param transactionId 微信支付订单号
     */
    private void wxPayCallBackHandle(String outTradeNo, String transactionId, WxPayConfigDTO wxPayConfigDTO) {

        // 如果有退款的情况 先集中组装参数
        WxPayReFoundReq wxPayReFoundReq = new WxPayReFoundReq();
        wxPayReFoundReq.setSub_mchid(wxPayConfigDTO.getSub_mch_id());
        wxPayReFoundReq.setOut_refund_no(RandomUtil.randomString(32));
        wxPayReFoundReq.setTransaction_id(transactionId);
        wxPayReFoundReq.setNotify_url(wxPayConfigDTO.getRefund_notify_url());

        // 【Dubbo调用订单服务】 查询订单信息
        OrderInfoResp orderInfoResp = wxPayOrderServiceApi.getOrderInfoByOutTradeNo(outTradeNo);

        // 如果查询订单失败 说明订单信息异常 直接退款给用户
        if (ObjectUtils.isEmpty(orderInfoResp) && ObjectUtils.isEmpty(orderInfoResp.getShopOrder()) && CollectionUtils.isEmpty(orderInfoResp.getShopOrderDetailsList())) {

            // 【openFeign调用微信订单查询接口 用来查询金额】
            WxPayStatusDTO wxPayStatusDTO = wxPayOpenFeignApi.wxPayStatusQuery(transactionId, wxPayConfigDTO.getSp_mch_id(), wxPayConfigDTO.getSub_mch_id());
            Integer payerTotal = wxPayStatusDTO.getAmount().getPayer_total();

            // 【openFeign调用微信退款接口】
            wxPayReFoundReq.setReason("商城订单信息异常，全额退款已发起");
            wxPayReFoundReq.setAmount(new WxPayReFoundAmountDTO(payerTotal, payerTotal, PayCurrencyEnum.CNY.getCode()));
            wxPayReFoundExecute(wxPayReFoundReq);
            return;
        }

        // 订单金额计算
        BigDecimal orderAmount = orderInfoResp.getShopOrderDetailsList().stream().map(ShopOrderDetails::getGoodsPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 转换为分
        Integer orderTotalMoney = orderAmount.multiply(BigDecimal.valueOf(100)).intValue();

        // 如果对于商城来说订单已经超时 则直接进行退款
        if (orderInfoResp.getShopOrder().getOrderStatus().equals(WxPayTradeStateEnum.CANCEL.getCode())) {

            // 【OpenFeign调用微信退款服务】执行退款操作
            wxPayReFoundReq.setReason("订单已超时，全额退款已发起");
            wxPayReFoundReq.setAmount(new WxPayReFoundAmountDTO(orderTotalMoney, orderTotalMoney, PayCurrencyEnum.CNY.getCode()));
            wxPayReFoundExecute(wxPayReFoundReq);

            // 【Dubbo调用订单服务】 修改订单信息为退款
            orderInfoResp.getShopOrder().setOrderStatus(WxPayTradeStateEnum.REFUND.getCode());
            wxPayOrderServiceApi.modifyOrderInfo(orderInfoResp);
            return;
        }

        // 商城中该订单当前状态不为SUCCESS 说明还未支付 则修改单据通过
        if (!WxPayTradeStateEnum.SUCCESS.getCode().equals(orderInfoResp.getShopOrder().getOrderStatus())) {
            orderInfoResp.getShopOrder().setOrderStatus(WxPayTradeStateEnum.SUCCESS.getCode());
            orderInfoResp.getShopOrder().setWxOrderCode(transactionId);

            // 【Dubbo调用订单服务】 修改订单信息
            wxPayOrderServiceApi.modifyOrderInfo(orderInfoResp);

            // 删除订单缓存值
            boolean tag = redisUtil.delete(ZHOUZHOU_ORDER_KEY + orderInfoResp.getShopOrder().getOrderCode());
            BizExUtil.requirefalse(!tag, "删除订单缓存失败");
        }
    }

    private void wxPayReFoundCallBackHandle(String transactionId) {

        // 【Dubbo调用订单服务】 查询订单信息
        OrderInfoResp orderInfoResp = wxPayOrderServiceApi.getOrderInfoByTransactionId(transactionId);
        orderInfoResp.getShopOrder().setOrderStatus(WxPayTradeStateEnum.REFUND.getCode());

        // 【Dubbo调用订单服务】 修改订单信息
        wxPayOrderServiceApi.modifyOrderInfo(orderInfoResp);
    }

    private void wxPayReFoundExecute(WxPayReFoundReq wxPayReFoundReq) {
        WxPayConfigDTO wxPayConfigDTO = wxUtil.wxPayConfigQuery();
        wxPayReFoundReq.setSub_mchid(wxPayConfigDTO.getSub_mch_id());
        wxPayReFoundReq.setNotify_url(wxPayConfigDTO.getRefund_notify_url());
        String result = wxPayOpenFeignApi.wxPayReFound(wxPayReFoundReq);
        log.info("微信支付退款返回结果：{}", result);
    }

    private void assembleWxReqParameter(WxPayOrderDTO wxPayOrderDTO, WxPayConfigDTO wxPayConfigDTO) {
        wxPayOrderDTO.setPayer(new WxPayPayerDTO(LoginUserContextHolder.get().getUserResp().getOpenId()));
        wxPayOrderDTO.setSp_appid(wxPayConfigDTO.getSp_app_id());
        wxPayOrderDTO.setSp_mchid(wxPayConfigDTO.getSp_mch_id());
        wxPayOrderDTO.setSub_appid(wxPayConfigDTO.getSub_app_id());
        wxPayOrderDTO.setSub_mchid(wxPayConfigDTO.getSub_mch_id());
        wxPayOrderDTO.setNotify_url(wxPayConfigDTO.getNotify_url());
    }
}
