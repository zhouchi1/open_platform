package com.zhouzhou.cloud.userservice.aspect;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhouzhou.cloud.common.entity.TenantAuth;
import com.zhouzhou.cloud.common.service.excepetions.BizException;
import com.zhouzhou.cloud.common.utils.BizExUtil;
import com.zhouzhou.cloud.userservice.annotation.SignPayloadType;
import com.zhouzhou.cloud.userservice.annotation.SignSaasIdentity;
import com.zhouzhou.cloud.common.mapper.TenantAuthMapper;
import com.zhouzhou.cloud.userservice.req.ProcessSaasReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Aspect
@Component
public class SignSaasIdentityAspect {

    @Resource
    private TenantAuthMapper tenantAuthMapper;

    @Around(value = "@annotation(signPayloadType) && @annotation(signSaasIdentity)", argNames = "joinPoint,signPayloadType,signSaasIdentity")
    public Object around(ProceedingJoinPoint joinPoint,
                         SignPayloadType signPayloadType,
                         SignSaasIdentity signSaasIdentity) throws Throwable {

        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof ProcessSaasReq)) {
            throw new RuntimeException("参数错误");
        }

        ProcessSaasReq<Object> dto = (ProcessSaasReq<Object>) args[0];
        String appId = dto.getAppId();
        String payloadJson = dto.getPayload();
        String timestamp = dto.getTimeStamp();
        String nonce = dto.getNonceStr();
        String signContext = dto.getSignContext();

        long requestTime = Long.parseLong(timestamp);
        if (Math.abs(System.currentTimeMillis() - requestTime) > 5 * 60 * 1000) {
            throw new RuntimeException("签名验证失败：请求过期");
        }

        String signingString = appId + payloadJson + timestamp + nonce;

        // 获取该saas平台公钥
        TenantAuth tenantAuth = tenantAuthMapper.selectOne(new LambdaQueryWrapper<TenantAuth>().eq(TenantAuth::getAppId, appId));
        BizExUtil.requirefalse(ObjectUtils.isEmpty(tenantAuth), "未找到该Saas平台授权信息");

        // 验签
        boolean verified = verify(signingString, signContext, tenantAuth.getAppPublicSecret());
        if (!verified) {
            throw new BizException("签名验证失败：签名不一致");
        }

        Class<?> targetClass = signPayloadType.value();
        Object parsedPayload = JSON.parseObject(payloadJson, targetClass);

        // 注入解析后的 payload
        dto.setParsedPayload(parsedPayload);

        return joinPoint.proceed();
    }

    private boolean verify(String content, String base64Signature, String base64PublicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(content.getBytes(StandardCharsets.UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(base64Signature);
        return signature.verify(signatureBytes);
    }
}
