package c.n.t.a.s.j.a.contants;

/**
 * @ClassName Constants
 * @Author Colain.Yin
 * @date 2025/9/11 11:50
 */
public class Constants {
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 新增能力入口
     */
    public static final  String REMOTE_NP_DEV_URL = "https://idcenter.newposp.com:8099/ability/openApi";

    /**
     * prod-sdk url
     */
    public static final  String REMOTE_NP_PROD_URL = "https://api.newposstore.com:18094/ability/openApi";

    /**
     * The constant CONTENT_TYPE.
     */
    public static final String CONTENT_TYPE = "Content-Type";
    /**
     * The constant CONTENT_TYPE_JSON.
     */
    public static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * UTF-8字符集
     */
    public static final String CHARSET_UTF8 = "UTF-8";

    public static final int MIN_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 500;
    public static final int MIN_PAGE_NO = 1;


//    public static final String SIGN_METHOD_HMAC = "HmacSHA1";
    /**
     * HmacSHA1 or HmacSHA256 or SignWithRSA
     */
    public static final String SIGN_METHOD = "HmacSHA256";
    public static final String REQ_HEADER_SDK_VERSION = "Sdk-Version";

    public static final String REQ_HEADER_SDK_TIMESTAMP_NS= "X-NS-TS";
    public static final String REQ_HEADER_SDK_AUTHAPP_NS= "X-NS-APPID";
    public static final String REQ_HEADER_SDK_AUTHSIG_NS= "X-NS-SIGN";
    public static final String REQ_HEADER_SDK_AUTHRAND_NS= "X-NS-RAND";
    public static final String REQ_HEADER_SDK_AUTHKEYID_NS= "X-NS-KEYID";
    public static final String REQ_HEADER_SDK_AUTHSN_NS= "X-NS-SN";
    /**
     * The constant ACCESS_LANGUAGE.
     */
    public static final String ACCESS_LANGUAGE = "Accept-Language";
    public static final String CONTENT_ENCODING_GZIP = "gzip";
    public static final String SDK_VERSION = "1.0.0";

}
