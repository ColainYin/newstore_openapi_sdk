package c.n.t.a.s.j.a.base.request;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SDKRequest
 * @Author Colain.Yin
 * @date 2025/9/11 14:03
 */
public class SDKRequest {
	
	private static final Logger logger = LoggerFactory.getLogger(SDKRequest.class.getSimpleName());

    /**
     * The request method is a upload API
     */
    protected boolean isUpload;

    /**
     * The Request method.
     */
    protected RequestMethod requestMethod = RequestMethod.GET;      // 请求方法
    /**
     * The Request mapping url.
     */
    protected String requestMappingUrl;                             // Request mapping url
    /**
     * The Header map.
     */
    protected Map<String, String> headerMap;                        // 请求头参数
    /**
     * The Request params.
     */
    protected Map<String, Object> requestParams;                    // 自定义表单参数
    /**
     * The Timestamp.
     */
    protected Long timestamp;                                       // 请求时间戳
    /**
     * The Request body.
     */
    protected String requestBody;                                   // Request body json string
    /**
     * The Save file path.
     */
    protected String saveFilePath;                                  // 文件保存路径
    /**
     * The Uploaded file path.
     */
    protected String uploadFilePath;                                // 文件上传路径
    /**
     * The Compress data.
     */
    protected boolean compressData = false;                         // 是否压缩数据
    /**
     * The Random data.
     */
    protected String rand;                                          //随机数

    /**
     * Token data getting from login api
     */
    protected String token;                                          //token

    /**
     * Instantiates a new Sdk request.
     */
    public SDKRequest() {
    }

    /**
     * Instantiates a new Sdk request.
     *
     * @param requestMappingUrl the request mapping url
     */
    public SDKRequest(String requestMappingUrl) {
        this.requestMappingUrl = requestMappingUrl;
    }

    /**
     * Instantiates a new Sdk request.
     *
     * @param requestMappingUrl the request mapping url
     * @param isUpload          a upload url or not
     */
    public SDKRequest(String requestMappingUrl, boolean isUpload, String uploadFilePath) {
        this.requestMappingUrl = requestMappingUrl;
        this.isUpload = isUpload;
        this.uploadFilePath = uploadFilePath;
    }

    /**
     * Gets request method.
     *
     * @return the request method
     */
    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    /**
     * Sets request method.
     *
     * @param requestMethod the request method
     */
    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    /**
     * Gets request mapping url.
     *
     * @return the request mapping url
     */
    public String getRequestMappingUrl() {
        return requestMappingUrl;
    }

    /**
     * Sets request mapping url.
     *
     * @param requestMappingUrl the request mapping url
     */
    public void setRequestMappingUrl(String requestMappingUrl) {
        this.requestMappingUrl = requestMappingUrl;
    }

    /**
     * Gets header map.
     *
     * @return the header map
     */
    public Map<String, String> getHeaderMap() {
        if (this.headerMap == null) {
            this.headerMap = new HashMap<String, String>();
        }
        return headerMap;
    }

    /**
     * Sets header map.
     *
     * @param headerMap the header map
     */
    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    /**
     * Add header.
     *
     * @param key   the key
     * @param value the value
     */
    public void addHeader(String key, String value){
        getHeaderMap().put(key, value);
    }

    /**
     * Gets request params.
     *
     * @return the request params
     */
    public Map<String, Object> getRequestParams() {
        if(this.requestParams == null){
            this.requestParams = new HashMap<String, Object>();
        }
        return requestParams;
    }

    /**
     * Sets request params.
     *
     * @param requestParams the request params
     */
    public void setRequestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    /**
     * Add request param.
     *
     * @param key   the key
     * @param value the value
     */
    public void addRequestParam(String key, Object value) {
        getRequestParams().put(key, value);
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets request body.
     *
     * @return the request body
     */
    public String getRequestBody() {
        return requestBody;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Sets request body.
     *
     * @param requestBody the request body
     */
    public void setRequestBody(String requestBody) {
    	//logger.debug("Request body is {}", requestBody);
        this.requestBody = requestBody;
    }

    /**
     * The enum Request method.
     */
    public enum RequestMethod{
        /**
         * Get request method.
         */
        GET("GET"), /**
         * Post request method.
         */
        POST("POST"), /**
         * Put request method.
         */
        PUT("PUT"), /**
         * Delete request method.
         */
        DELETE("DELETE");

        private String value;
        RequestMethod(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * Gets save file path.
     *
     * @return the save file path
     */
    public String getSaveFilePath() {
        return saveFilePath;
    }

    /**
     * Sets save file path.
     *
     * @param saveFilePath the save file path
     */
    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    /**
     * Is compress data boolean.
     *
     * @return the boolean
     */
    public boolean isCompressData() {
        return compressData;
    }

    /**
     * Sets compress data.
     *
     * @param compressData the compress data
     */
    public void setCompressData(boolean compressData) {
        this.compressData = compressData;
    }


    public String getRand() {
        return rand;
    }

    public void setRand(String rand) {
        this.rand = rand;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }
}
