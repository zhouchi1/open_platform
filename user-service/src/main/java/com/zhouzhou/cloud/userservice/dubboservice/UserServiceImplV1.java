package com.zhouzhou.cloud.userservice.dubboservice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhouzhou.cloud.common.dto.UserIdentityConfirmDTO;
import com.zhouzhou.cloud.common.entity.TenantAuth;
import com.zhouzhou.cloud.common.entity.UserInfo;
import com.zhouzhou.cloud.common.service.interfaces.UserServiceApi;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;
import com.zhouzhou.cloud.common.mapper.TenantAuthMapper;
import com.zhouzhou.cloud.common.mapper.UserInfoMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.*;

@Slf4j
@DubboService(version = "1.0.0")
public class UserServiceImplV1 implements UserServiceApi {

    @Resource
    private TenantAuthMapper tenantAuthMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Boolean authConfirm(UserIdentityConfirmDTO userIdentityConfirmDTO) throws Exception {
        // 时间戳过期时间判定
        long currentTime = Instant.now().toEpochMilli();
        if (Math.abs(currentTime - userIdentityConfirmDTO.getTimestamp()) > 300_000) {
            return false;
        }

        // 查询内部唯一标识
        LambdaQueryWrapper<TenantAuth> tenantQueryWrapper = new LambdaQueryWrapper<>();
        tenantQueryWrapper.eq(TenantAuth::getAppId, userIdentityConfirmDTO.getAppId());
        TenantAuth tenantAuth = tenantAuthMapper.selectOne(tenantQueryWrapper);

        // 不存在的第三方 拒绝授权
        if (ObjectUtils.isEmpty(tenantAuth)) {
            return false;
        }

        // 不存在的第三方用户 拒绝授权
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getId, userIdentityConfirmDTO.getUserId());
        queryWrapper.eq(UserInfo::getAppId, tenantAuth.getAppId());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(userInfo)) {
            return false;
        }

        // 根据加密算法匹配匹配是否为合法请求
        SortedMap<String, String> sortedParams = new TreeMap<>();
        sortedParams.put("appId", userIdentityConfirmDTO.getAppId());
        sortedParams.put("nonce", userIdentityConfirmDTO.getNonce());
        sortedParams.put("timestamp", String.valueOf(userIdentityConfirmDTO.getTimestamp()));
        sortedParams.put("userId", userIdentityConfirmDTO.getUserId());

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (entry.getValue() != null) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        // 去掉最后的 "&"
        String dataToVerify = sb.substring(0, sb.length() - 1);

        // 解析公钥
        PublicKey publicKey = getPublicKeyFromString(tenantAuth.getAppPublicSecret());

        // 验证签名
        return verifySignature(dataToVerify, userIdentityConfirmDTO.getSign(), publicKey);
    }

    @Override
    public SystemUserResp queryUserInfo(UserIdentityConfirmDTO userIdentityConfirmDTO) {
        // 查询内部唯一标识
        LambdaQueryWrapper<TenantAuth> tenantQueryWrapper = new LambdaQueryWrapper<>();
        tenantQueryWrapper.eq(TenantAuth::getAppId, userIdentityConfirmDTO.getAppId());
        TenantAuth tenantAuth = tenantAuthMapper.selectOne(tenantQueryWrapper);

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getId, userIdentityConfirmDTO.getUserId());
        queryWrapper.eq(UserInfo::getAppId, tenantAuth.getAppId());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

        SystemUserResp systemUserResp = new SystemUserResp();
        systemUserResp.setUserId(String.valueOf(userInfo.getId()));
        systemUserResp.setAppId(tenantAuth.getAppId());
        return systemUserResp;
    }

    private PublicKey getPublicKeyFromString(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String fixString = publicKeyString.replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(fixString);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private boolean verifySignature(String data, String signBase64, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signBytes = Base64.getDecoder().decode(signBase64);
        return signature.verify(signBytes);
    }

}
