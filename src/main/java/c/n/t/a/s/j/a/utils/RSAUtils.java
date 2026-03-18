package c.n.t.a.s.j.a.utils;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RSAUtils
 * @Author Colain.Yin
 * @date 2025/10/28 15:12
 */
@Slf4j
public final class RSAUtils {

    /**
     * 密钥生成器算法
     */
    public final static String KEY_ALGORITHM = "RSA";
    public static final String IOT_PUBLIC_KEY = "iot_publicKey";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";
    public static final String PASSWORD = "";
    public static final String KEY_SIZE = "keySize";
    public static final int keySizeInit = 2048;

    /**
     * 填充方式
     */
    public static final String KEY_ALGORITHM_PADDING = "RSA/ECB/PKCS1Padding";


    /**
     * 签名加密算法
     */
    public final static String SIGN_ALGORITHM_SHA256WITHRSA = "SHA256withRSA";

    /**
     * @Title: signSha256WithRsa
     * @Description: 生成数据签名
     * @param data 需签名的数据
     * @param privateKey 加密签名数据私钥
     * @return String 签名数据
     */
    public static String signSha256WithRsa(byte[] data, String privateKey) throws Exception {
        return sign(data, privateKey);
    }

    /**
     * @Title: sign
     * @Description: 生成数据签名
     * @param data 需签名的数据
     * @param privateKey 加密签名数据私钥
     * @return String 签名数据
     */
    private static String sign(byte[] data, String privateKey) throws Exception {
        // 私钥字符串转字节数组
        byte[] keyBytes = new Base64().decode(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(RSAUtils.SIGN_ALGORITHM_SHA256WITHRSA);
        signature.initSign(priKey);
        signature.update(data);
        byte[] signByte = signature.sign();
        return Base64.encode(signByte);
    }


    /**
     * @Title:signVerifySha256WithRsa
     * @Description: 签名验证
     * @param signSrc 需签名的数据
     * @param publicKey 验证签名数据公钥
     * @param sign 签名数据
     * @return boolean true-验证成功 false-验证失败
     */
    public static boolean signVerifySha256WithRsa(byte[] signSrc, String publicKey, String sign) throws Exception {
        return signVerify(signSrc, publicKey, sign);
    }

    /**
     * @Title:signVerify
     * @Description: 签名验证
     * @param signSrc 需签名的数据
     * @param publicKey 验证签名数据公钥
     * @param sign 签名数据
     * @return boolean true-验证成功 false-验证失败
     */
    private static boolean signVerify(byte[] signSrc, String publicKey, String sign) throws Exception {
        // 公钥字符串转字节数组
        byte[] keyBytes = new Base64().decode(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey myPublicKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(RSAUtils.SIGN_ALGORITHM_SHA256WITHRSA);
        signature.initVerify(myPublicKey);
        signature.update(signSrc);
        // 验证签名
        return signature.verify(new Base64().decode(sign));
    }






    /**
     * 生成密钥对
     *
     * @return
     */
    public static Map<String, String> generateKeyBytes() {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(keySizeInit);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, String> keyMap = new HashMap<>();
            keyMap.put(PUBLIC_KEY, Base64.encode(publicKey.getEncoded()));
            keyMap.put(PRIVATE_KEY, Base64.encode(privateKey.getEncoded()));  // FormatConvert.base64Encode
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            log.error("[generateKeyBytes error:]",e);
            //e.printStackTrace();
        }
        return null;
    }


    /**
     * 从字符串解码公钥
     *
     * @param publicKey 公钥
     * @return 公钥
     * @throws Exception Exception
     */
    public static PublicKey decodePublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * 从字符串解码私钥
     *
     * @param privateKey 密钥
     * @return base64后的字符串
     * @throws Exception Exception
     */
    public static PrivateKey decodePrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(pkcs8KeySpec);
    }


    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param pubKey 公钥(BASE64编码)
     * @return 加密后的值
     * @throws Exception Exception
     */
    public static String encryptByPublicKey(byte[] data, String pubKey) throws Exception {
        byte[] decoded = Base64.decode(pubKey);
        RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encode(cipher.doFinal(data));
    }

    /**
     * 私钥解密
     *
     * @param inputByte       源数据
     * @param priKey 私钥(BASE64编码)
     * @return 解密后的值
     * @throws Exception Exception
     */
    public byte[] decryptByPrivateKey(byte[] inputByte, String priKey) throws Exception {
        //base64编码的私钥
        byte[] decoded = Base64.decode(priKey);
        RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return  cipher.doFinal(inputByte);
    }

    /**
     * 私钥解密
     *
     * @param inputByte       源数据
     * @param priKey 私钥(BASE64编码)
     * @return 解密后的值
     * @throws Exception Exception
     */
    public static byte[] decryptByPvk(byte[] inputByte, String priKey) throws Exception {
        //base64编码的私钥
        byte[] decoded = Base64.decode(priKey);
        RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(inputByte);
    }


    /**
     * 私钥解密
     *
     * @param inputByte       源数据
     * @param priKey 私钥(BASE64编码)
     * @return 解密后的值
     * @throws Exception Exception
     */
    public static byte[] decryptByPrivateKeyNew(String inputByte, String priKey) throws Exception {
        //base64编码的私钥
        byte[] decoded = Base64.decode(priKey);
        RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] data = Base64.decode(inputByte);
        return getMaxResultDecrypt(data,cipher);
    }
    //长度过长分割解密
    private static byte[] getMaxResultDecrypt(byte[] inputArray, Cipher cipher) throws Exception {
        int inputLength = inputArray.length;
        // 最大解密字节数，超出最大字节数需要分组加密
        int MAX_ENCRYPT_BLOCK = 256;
        // 标识
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return resultBytes;
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


    public static void main(String[] args) {

        try {
            //随机产生RSA公钥
            Map<String, String> rsa = RSAUtils.generateKeyBytes();
            System.out.println("private Key1:" + rsa.get(PUBLIC_KEY));
            System.out.println("public Key2:" + rsa.get(PRIVATE_KEY));


//            String data="6E20A0C6AF0C29C1A8799AFEA7052B602A45E771A7FC310DA3F2F5307513EF12";
//            //私钥签名&公钥验签
////            String sign = RSAUtils.signSha256WithRsa(hashSHA256(data), privateKey);
//            byte[] bytes = hashSHA256(data);
////			System.out.println(sign);
//			System.out.println("bytes:" + getBytesHex(bytes));
////
////			boolean result = RSAUtils.signVerifySha256WithRsa(bytes, publicKey, sign);
////            System.out.println("sign verify:" + result);
//
//			Sign signUtil = SecureUtil.sign(SignAlgorithm.SHA256withRSA,privateKey,publicKey);
//			byte[] sign = signUtil.sign(data.getBytes("utf-8"));
//
//			boolean rest = signUtil.verify(data.getBytes("utf-8"), sign);
//
//			String signstring = Base64.encodeBase64String(sign);
//			System.out.println("signstring:"+signstring);
//			System.out.println("verify:"+rest);



            //公钥加密&私钥解密
//            data = "92EBBED3B9B75588BC07020BA3569B8D";
//            byte[] binary = data.getBytes();
//            byte[] fillArray = new byte[RSAUtils.keySizeInit / 8 - 11];
//            Arrays.fill(fillArray, (byte) 0x20);
//            System.arraycopy(binary, 0, fillArray, 0, data.length());
//
//            String encodeData = RSAUtils.encryptByPublicKey(fillArray, publicKey);
//            System.out.println("encodeData:" + encodeData);
//
//            byte[] tmpBytes = Base64.decodeBase64(encodeData);
//            String decodeData = RSAUtils.decryptByPrivateKey(tmpBytes, privateKey);
//            System.out.println("decodeData:" + decodeData);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
