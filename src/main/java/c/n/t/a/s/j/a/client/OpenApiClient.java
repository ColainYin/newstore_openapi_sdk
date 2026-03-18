package c.n.t.a.s.j.a.client;

import c.n.t.a.s.j.a.base.request.SDKRequest;
import c.n.t.a.s.j.a.contants.Constants;
import c.n.t.a.s.j.a.contants.ResultCode;
import c.n.t.a.s.j.a.utils.BaseTmsSysApi;
import c.n.t.a.s.j.a.utils.DESUtils;
import c.n.t.a.s.j.a.utils.EnhancedJsonUtils;
import c.n.t.a.s.j.a.utils.SignatureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

import static c.n.t.a.s.j.a.contants.Constants.*;

/**
 * @ClassName OpenApiClient
 * @Author Colain.Yin
 * @date 2025/9/11 13:56
 */
public class OpenApiClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiClient.class.getSimpleName());

    /**
     * The Base url.
     */
    protected String baseUrl;
    /**
     * The Api key.
     */
    protected String apiKey;
    /**
     * The Api secret.
     * If use hmac algorithm, only 1 String
     * If use rsa algorithm, first is local private key, the second is the peer public key
     */
    protected String[] apiSecret;

    /**
     * The Sign method.
     */
    protected String signMethod = Constants.SIGN_METHOD;
    /**
     * The Connect timeout.
     */
    protected int connectTimeout = 5000; 			// 默认连接超时时间为30秒
    /**
     * The Read timeout.
     */
    protected int readTimeout = 30000; 				// 默认响应超时时间为30秒

    protected int retryTimes = 5;

    /**
     * Instantiates a new Default client.
     *
     * @param baseUrl   the base url
     */
    public OpenApiClient(String baseUrl, String apiKey, String[] apiSecret) {
        this.apiSecret = apiSecret;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        if(BaseTmsSysApi.connectTimeout>0) {
            this.connectTimeout = BaseTmsSysApi.connectTimeout;
        }
        if(BaseTmsSysApi.readTimeout>0) {
            this.readTimeout = BaseTmsSysApi.readTimeout;
        }
        retryTimes = BaseTmsSysApi.retryTimes;
    }

    /**
     * Instantiates a new Default client.
     *
     * @param baseUrl        the base url
     * @param apiKey         the app key
     * @param apiSecret      the app secret
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     */
    public OpenApiClient(String baseUrl, String apiKey, String[] apiSecret, int connectTimeout, int readTimeout) {
        this(baseUrl, apiKey, apiSecret);
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    /**
     * Instantiates a new Default client.
     *
     * @param baseUrl        the base url
     * @param appKey         the app key
     * @param appSecret      the app secret
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     * @param signMethod     the sign method
     */
    public OpenApiClient(String baseUrl, String appKey, String[] appSecret, int connectTimeout, int readTimeout, String signMethod) {
        this(baseUrl, appKey, appSecret, connectTimeout, readTimeout);
        this.signMethod = signMethod;
    }

    /**
     * Execute string.
     *
     * @param request the request
     * @return the string
     */
    public String execute(SDKRequest request) {
        try {
            return execute0(request);
        } catch (GeneralSecurityException e) {
            logger.error("GeneralSecurityException occurred when execute request. Details: {}", e.toString());
        } catch (Exception e){
            logger.error("Exception occurred when execute request. Details", e);
        }
        return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
    }

    String prepare(SDKRequest request) throws Exception {
        String requestUrl = "";
        if (SDKRequest.RequestMethod.GET.equals(request.getRequestMethod())) {
            String params = HttpUtils.buildGetQuery(request.getRequestParams(), Constants.CHARSET_UTF8);
            requestUrl = HttpUtils.buildRequestUrl(baseUrl + request.getRequestMappingUrl(), params);
        } else {
            requestUrl = HttpUtils.buildRequestUrl(baseUrl + request.getRequestMappingUrl());
        }

        Long timestamp = request.getTimestamp();
        if (timestamp == null) {
            timestamp = System.currentTimeMillis() / 1000;
        }
        request.addHeader(REQ_HEADER_SDK_TIMESTAMP_NS, Long.toString(timestamp));
        //request.addHeader(REQ_HEADER_SDK_AUTHAPP_NS, apiKey);

        byte[] binary = DESUtils.generateDES();
        String rand = Base64.getEncoder().encodeToString(binary);
        request.addHeader(REQ_HEADER_SDK_AUTHRAND_NS, rand);
        // 核心参数之一
        request.addHeader(REQ_HEADER_SDK_AUTHKEYID_NS, apiKey);

        //请求签名
        String signature = "";
        String signBody = SignatureUtils.buildSignBodySdk(request);
        signature = SignatureUtils.generateSignSdk(signMethod, request.getRequestMethod().getValue(),
                request.getRequestMappingUrl(), signBody, apiKey, apiSecret, rand, timestamp);
        request.addHeader(REQ_HEADER_SDK_AUTHSIG_NS, signature);
        // 设置默认开启gzip接收，压缩响应体内容
        request.addHeader("Accept-Encoding", "gzip");
        // 如果超过1024字节，则gzip压缩
        if (signBody.getBytes(StandardCharsets.UTF_8).length > 1024) {
            request.addHeader("Content-Encoding", "gzip");
            request.setCompressData(true);
        }
        logger.info(" --> {} {}", request.getRequestMethod().getValue(), requestUrl);
        return requestUrl;
    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected String execute0(SDKRequest request) throws Exception {
        String response;
        String requestUrl = prepare(request);
        if(request.isUpload()){
            response = HttpUtils.uploadRequest(requestUrl, request.getRequestMethod().getValue(), connectTimeout, readTimeout, request.getRequestParams(), request.getHeaderMap(), request.getUploadFilePath(), retryTimes);
        }else {
            if (!request.isCompressData()) {
                response = HttpUtils.request(requestUrl, request.getRequestMethod().getValue(), connectTimeout, readTimeout, request.getRequestBody(), request.getHeaderMap(), request.getSaveFilePath(), retryTimes);
            } else {
                response = HttpUtils.compressRequest(requestUrl, request.getRequestMethod().getValue(), connectTimeout, readTimeout, request.getRequestBody(), request.getHeaderMap(), request.getSaveFilePath(), retryTimes);
            }
        }
        boolean verify = SignatureUtils.verifySignSdk(signMethod, response, apiKey, apiSecret,
                request.getHeaderMap().get(REQ_HEADER_SDK_AUTHRAND_NS),
                request.getHeaderMap().get(REQ_HEADER_SDK_AUTHSIG_NS),
                request.getHeaderMap().get(REQ_HEADER_SDK_TIMESTAMP_NS));
        if (!verify) {
            throw new Exception("verify Sign not passed");
        }
        return response;
    }

    /**
     * 设置API请求的连接超时时间
     *
     * @param connectTimeout the connect timeout
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 设置API请求的读超时时间
     *
     * @param readTimeout the read timeout
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

}
