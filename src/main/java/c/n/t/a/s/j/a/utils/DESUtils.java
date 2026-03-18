package c.n.t.a.s.j.a.utils;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

import static cn.hutool.crypto.Padding.NoPadding;

/**
 * @ClassName DESUtils
 * @Author Colain.Yin
 * @date 2025/10/17 15:12
 */
@Slf4j
public class DESUtils {

    public static byte[] generateDES()
    {
        return SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();
    }

    /*
     * 超长内容DES循环加密
     */
    public static String encryptBinary(byte[] desKey, byte[] binary) throws Exception {
        int data_in_len = binary.length;
        int data_in = 0, n = 0;
        byte[] temp_data_in = new byte[8];
        byte[] temp_data_out = null;
        DES key1 = null, key2  = null, key3  = null;
        int    encryptLen = binary.length % 8 == 0 ? binary.length : binary.length - (binary.length % 8) + 8;
        byte[] encrypt = new byte[encryptLen];

        key1 = new DES(Mode.ECB, NoPadding, desKey);
        if(desKey.length == 16) {
            System.arraycopy(desKey, 8, temp_data_in, 0, 8);
            key2 = new DES(Mode.ECB, NoPadding, temp_data_in);
        }
        if(desKey.length == 24) {
            System.arraycopy(desKey, 8, temp_data_in, 0, 8);
            key2 = new DES(Mode.ECB, NoPadding, temp_data_in);
            System.arraycopy(desKey, 16, temp_data_in, 0, 8);
            key3 = new DES(Mode.ECB, NoPadding, temp_data_in);
        }
        while (data_in_len > 0) {
            if (data_in_len >= 8) {
                System.arraycopy(binary, data_in, temp_data_in, 0, 8);
                data_in_len -= 8;
            } else {
                System.arraycopy(binary, data_in, temp_data_in, 0, data_in_len);
                for(n = data_in_len; n < temp_data_in.length; n++){
                    temp_data_in[n] = 0x00;
                }
                data_in_len = 0;
            }
            switch(desKey.length){
                case 8:
                    temp_data_out = key1.encrypt(temp_data_in);
                    System.arraycopy(temp_data_out, 0, encrypt, data_in, temp_data_out.length);
                    break;
                case 16:
                    temp_data_out = key1.encrypt(temp_data_in);
                    temp_data_in = key2.decrypt(temp_data_out);
                    temp_data_out = key1.encrypt(temp_data_in);
                    System.arraycopy(temp_data_out, 0, encrypt, data_in, temp_data_out.length);
                    break;
                case 24:
                    temp_data_out = key1.encrypt(temp_data_in);
                    temp_data_in = key2.decrypt(temp_data_out);
                    temp_data_out = key3.encrypt(temp_data_in);
                    System.arraycopy(temp_data_out, 0, encrypt, data_in, temp_data_out.length);
                    break;
                default:
                    break;
            }
            data_in += 8;
        }
        return Base64.getEncoder().encodeToString(encrypt);
    }

    /*
     * 超长内容DES循环解密
     */
    public static String decodeBinary(byte[] desKey, byte[] binary) throws Exception {
        int data_in_len = binary.length;
        int data_in = 0, n = 0;
        byte[] temp_data_in = new byte[8];
        byte[] temp_data_out = null;
        DES key1 = null, key2  = null, key3  = null;
        int    encryptLen = binary.length % 8 == 0 ? binary.length : binary.length - (binary.length % 8) + 8;
        byte[] encrypt = new byte[encryptLen];

        key1 = new DES(Mode.ECB, NoPadding, desKey);
        if(desKey.length == 16) {
            System.arraycopy(desKey, 8, temp_data_in, 0, 8);
            key2 = new DES(Mode.ECB, NoPadding, temp_data_in);
        }
        if(desKey.length == 24) {
            System.arraycopy(desKey, 8, temp_data_in, 0, 8);
            key2 = new DES(Mode.ECB, NoPadding, temp_data_in);
            System.arraycopy(desKey, 16, temp_data_in, 0, 8);
            key3 = new DES(Mode.ECB, NoPadding, temp_data_in);
        }
        while (data_in_len > 0) {
            if (data_in_len >= 8) {
                System.arraycopy(binary, data_in, temp_data_in, 0, 8);
                data_in_len -= 8;
            } else {
                System.arraycopy(binary, data_in, temp_data_in, 0, data_in_len);
                for(n = data_in_len; n < temp_data_in.length; n++){
                    temp_data_in[n] = 0x00;
                }
                data_in_len = 0;
            }
            switch(desKey.length){
                case 8:
                    temp_data_out = key1.decrypt(temp_data_in);
                    System.arraycopy(temp_data_out, 0, encrypt, data_in, temp_data_out.length);
                    break;
                case 16:
                    temp_data_out = key1.decrypt(temp_data_in);
                    temp_data_in = key2.encrypt(temp_data_out);
                    temp_data_out = key1.decrypt(temp_data_in);
                    System.arraycopy(temp_data_out, 0, encrypt, data_in, temp_data_out.length);
                    break;
                case 24:
                    temp_data_out = key1.decrypt(temp_data_in);
                    temp_data_in = key2.encrypt(temp_data_out);
                    temp_data_out = key3.decrypt(temp_data_in);
                    System.arraycopy(temp_data_out, 0, encrypt, data_in, temp_data_out.length);
                    break;
                default:
                    break;
            }
            data_in += 8;
        }
        return Base64.getEncoder().encodeToString(encrypt);
    }

}
