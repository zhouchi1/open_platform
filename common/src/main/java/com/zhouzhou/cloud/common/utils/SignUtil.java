package com.zhouzhou.cloud.common.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SignUtil {

    public static String generateSignature(Map<String, String> params, String secret) {
        SortedMap<String, String> sortedParams = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (entry.getValue() != null) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        sb.append("secret=").append(secret); // 加上密钥

        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256HMAC.init(secretKey);
            byte[] hash = sha256HMAC.doFinal(sb.toString().getBytes());
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("签名生成失败", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
