import com.zhouzhou.cloud.WebsocketApplication;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.*;

@Slf4j
@SpringBootTest(classes = WebsocketApplication.class)
public class TestApplication {

    @Test
    public void main() {

        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String appId = "1";
        String nonce = "asdbfjhabsdjhfbaj";
        String userId = "1";

        // 构造参数 map
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        params.put("nonce", nonce);
        params.put("timestamp", timestamp);
        params.put("userId", userId);

        log.info(timestamp);

        String privateSecret = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDUEH2APwgmWXdC2CcXTZQX/VhpSsuEyVgpa2Npj/T5PtKFiLpZZkrVACy2CjE3Eu+Dg+VUEW28kuC3wfcRb8isYVYqIBuMpmxa+VhLhJ8WQfR5T8ZPxGh4kpru2I/atvkGmE9K47XsJKHb12+2jEkNFWaU3xu0xBqLZxM+ZdkXU42GxLXa0xaxckA7PIDgFWSz15Svy3SRZAsQtT8hAbNDpzV+muuDEBWr7851S4EG/sIVAuvLITSL5mqCaBaUl4d6PnL3Q6Qun7GA1B6gWMfxkNnZEx/pPB4MW8g9NIHK0e+hjDbXyt/tRqld9HtE9Y9TDTclE1auC+WdX5AhrASZAgMBAAECggEACeCUv97vtb2QCPyBYZv67F0f+5OAxDgOk6tNT5LlcaCD9LVIHLbfgGlIK9hwr2SB5GnFyjMfndtnfRebNuQygBjuhvrE3bm4rUoU2zubn1FGHkphbcCzrv4BGmPa23CPS8fXVaEijZOtOEyiBuok0ueuxftYUoSZyelso4jh4iyr34PRgnYBJeG7E7S8Fbogdzh2uAWCuTes95skmTnaBjtbek5lP7z2sDqo4i3P9Pf7J4NW7nRfDRXNlYW9rNR9Wb+g1ZM1CyqC6K+lkdBuXNIczLitAQXSBQVDjE7jRcGtXAl8E90GF0nJw2RftrN90obi3J4eOhw631yY8/OzQQKBgQDf78lSamTmwddMZ56iUkbekkl+TErvDdsSnSzerq7B/SMAw2Wh641n8JyJdSajsAdes8t7oH0LKytt/Sb8J+/U6UPVwChAAnmPkMtTVqETM+hBRR1k+vMYOk3WO/ryVcupO16iYgAa5fbD89tzdeGCta6qV7fepSoa5GVS/M2JSQKBgQDybYlJgFJga3wtXSFYqmMeJoJPNXuCz5wTjufXNhzT/9PcXXT6lEOIsFoC4tEHiuJpVa9/HKb4fOEsS/Jg8xPz9iootXJ50gfZy+zt4sW9GzZ8lKy/htoF4DQ5aQ5SI6MYFlnZ9KFiwPV8EGRVQKg863kE2wdy0N0DUv/U2ldw0QKBgQDPBBPfzZ1zuZS3/D6pHliHn0KzsAHB1tRD8+sr/N5vVP0L0pbcxi4V/VgotpU+0xctFBczNV0DeygN6O4mQyEyeWYNbzOGYQNRlfBxHW+QmfbGiAcxd9BNAHE+cHp/lpLM4qaMhP5F33ttEL05kqsjs6NBtA+1kohHDgchXbUAiQKBgCjnloySXFPsju1Qk3nUdhj2QnJ/AbIWua0LJc9IUCDtP6AlHMHTFnoP14A5KS/unTlMveniucGfwJaq0eDN4wX8+SwZQZGQgMadBAM3iknYN59ZrrQ6Wrs3E1jEgygU30YHL235OungyQr/8Q35dOmiia7qDh2vZDtVW9lcZfihAoGBAMfXNOVE10G2iUldV0P+pzWYjinMhxfKiITz3Z8ATgkO431XjT74o5pUUsPWvB5AGhBdOBIahlI0/oPQ+1LSC5IwvA1plUHEuO9+BAGGNOrsNoK4hcSl9ytUgq9V8vKknK7GZronFojaJdER02LNBpnbG6wu3G/urq4AWhw+Tw0p";
        SortedMap<String, String> sortedParams = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (entry.getValue() != null) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        String dataToVerify = sb.substring(0, sb.length() - 1); // 去掉最后的 "&"

        try {

            String fixString = privateSecret.replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(fixString);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(dataToVerify.getBytes(StandardCharsets.UTF_8));

            byte[] signBytes = signature.sign();

            String signBase64 = Base64.getEncoder().encodeToString(signBytes);

            log.info(signBase64);
        } catch (Exception e) {
            throw new RuntimeException("签名生成失败", e);
        }
    }

    @Test
    public void main1() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // 转换为 Base64 字符串
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        System.out.println("Private key: " + privateKeyStr);
        System.out.println("Public key: " + publicKeyStr);
    }
}
