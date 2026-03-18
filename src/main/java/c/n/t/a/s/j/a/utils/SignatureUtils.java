package c.n.t.a.s.j.a.utils;

import com.alibaba.fastjson.JSON;
import c.n.t.a.s.j.a.base.request.SDKRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;

/**
 * @ClassName BaseService
 * @Author Colain.Yin
 * @date 2025/10/12 10:50
 */
public class SignatureUtils {
    private static final Logger logger = LoggerFactory.getLogger(SignatureUtils.class.getSimpleName());


    /**
     * @param method  HmacSHA1 or HmacSHA256
     * @param hash
     * @param appKey
     * @return
     * @throws Exception
     */
    private static String generateHmacSign(String method, byte[] hash, String appKey) throws Exception {
        Mac hmac = Mac.getInstance(method);
        SecretKeySpec key = new SecretKeySpec(appKey.getBytes(StandardCharsets.UTF_8), method);
        hmac.init(key);
        byte[] array = hmac.doFinal(hash);
        return Base64.getEncoder().encodeToString(array);
    }

    /**
     * 通过SHA256计算hash值
     *
     * @param puredata 需要计算hash值的数据
     * @return hash值字节数组
     * @throws Exception exception
     */
    public static byte[] hashSHA256(String puredata) throws Exception {
        MessageDigest object = MessageDigest.getInstance("SHA-256");
        return object.digest(puredata.getBytes(StandardCharsets.UTF_8));
    }

    private static boolean verifyInOne(String method, byte[] hash, String[] appKey, String sign) throws Exception {
        String hmacSign = null;

        if(method.startsWith("SignWithRSA") || appKey.length == 2){
            return RSAUtils.signVerifySha256WithRsa(hash, appKey[1], sign);
        }else if(method.startsWith("HmacSHA")){
            hmacSign = generateHmacSign(method, hash, appKey[0]);
            logger.info("hmacSign={}", hmacSign);
            return hmacSign.equalsIgnoreCase(sign);
        }
        return false;
    }

    private static String signatureInOne(String method, byte[] hash, String[] appKey) throws Exception {
        if(method.startsWith("SignWithRSA") || appKey.length == 2){
            return RSAUtils.signSha256WithRsa(hash, appKey[0]);
        }else if(method.startsWith("HmacSHA")){
            return generateHmacSign(method, hash, appKey[0]);
        }
        return null;
    }

    public static boolean verifySign(String method, String body, String appId, String[] appKey, String rand, String sign, String timestamp) {
        boolean verify = false;
        try {
            // 验签
            String pureData = String.format("%s\n%s\n%s\n%s\n",
                    appId,
                    rand,
                    timestamp,
                    body);
            logger.debug("verify response sign:[\n{}]", pureData);
            verify = verifyInOne(method, hashSHA256(pureData), appKey, sign);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return verify;
    }

    /**
     * 验证SDK请求的签名
     * @param method
     * @param body
     * @param keyId
     * @param keySecret
     * @param rand
     * @param sign
     * @param timestamp
     * @return
     */
    public static boolean verifySignSdk(String method, String body, String keyId, String[] keySecret, String rand, String sign, String timestamp) {
        boolean verify = false;
        try {
            // 验签
            String pureData = String.format("%s%s%s%s%s",
                    body,
                    keyId,
                    timestamp,
                    rand,
                    keySecret[0]
                    );
            //logger.info("verify response sign:[\n{}]", pureData);
            String signResp = sha256Hmac(pureData, keySecret[0]);
            verify = signResp.equalsIgnoreCase(sign);
        } catch (Exception e) {
            logger.error("verifySignSdk-error", e);
        }
        return verify;
    }

    public static String generateSign(String method,String methodType,
                                      String apiName, String body, String appId, String[] appKey, String rand, Long timestamp) {
        String respSign = "";
        try {
            body = StringUtils.isNotEmpty(body) ? body : "";
            // 验签
            /*String pureData = String.format("%s\n%s\n%d\n%s\n",
                    appId,
                    rand,
                    timestamp,
                    body);*/
            String pureData = String.format("%s%s%s%s%d%s%s",methodType, apiName, body, appId, timestamp, rand, appKey[0]);
            logger.debug("generate request sign:[\n{}]", pureData);
            respSign = signatureInOne(method, hashSHA256(pureData), appKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respSign;
    }

    public static String generateSignSdk(String method,String methodType,
                                      String apiName, String body, String keyId, String[] appKey, String rand, Long timestamp) {
        String respSign = "";
        try {
            body = StringUtils.isNotEmpty(body) ? body : "";
            if (StringUtils.isNotEmpty(apiName) && apiName.startsWith("/")) {
                // 去掉前面的/
                apiName = apiName.substring(1);
            }
            String pureData = String.format("%s%s%s%s%d%s%s",methodType, apiName, body, keyId, timestamp, rand, appKey[0]);
            logger.debug("generate request sign:[\n{}]", pureData);
            respSign = sha256Hmac(pureData, appKey[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respSign;
    }

    public static String sha256Hmac(String message, String secret) {
        byte[] bytes = null;

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            bytes = mac.doFinal(message.getBytes());
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String buildSignBody(SDKRequest request, String requestUrl) throws Exception {
        int index = requestUrl.indexOf("/ability/openApi");
        if(index < 0){
            throw new Exception("api format error");
        }
        String tmp = requestUrl.substring(index);
        // 获取body
        String body = request.getRequestBody();
        if (StringUtils.isEmpty(body) && request.isUpload()) {
            // 如果body为空，则获取请求参数
            Map<String, Object> p =  request.getRequestParams();
            if(p != null && !p.isEmpty()) {
                // 转json
                String userData = JSON.toJSONString(p);
                body = Base64.getEncoder().encodeToString(userData.getBytes("UTF-8"));
            }
        }
        body = body == null ? "" : "\n" + body;
        return String.format("%s %s%s", request.getRequestMethod(), tmp, body);
    }

    public static String buildSignBodySdk(SDKRequest request) throws Exception {
        return request.getRequestBody();
    }
}
