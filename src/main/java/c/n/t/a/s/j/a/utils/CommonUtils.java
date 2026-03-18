package c.n.t.a.s.j.a.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName CommonUtils
 * @Author Colain.Yin
 * @date 2025/9/12 9:04
 */
public class CommonUtils {
    /**
     * byte数组转hex
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes){
        String strHex;
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            strHex = Integer.toHexString(aByte & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }

    public static boolean areNotEmpty(String name, String value) {
        return StringUtils.isNotEmpty(name) || StringUtils.isNotEmpty(value);
    }
}
