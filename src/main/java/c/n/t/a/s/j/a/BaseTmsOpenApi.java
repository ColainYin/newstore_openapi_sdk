package c.n.t.a.s.j.a;

import c.n.t.a.s.j.a.base.dto.PageRequest;
import c.n.t.a.s.j.a.base.request.SDKRequest;
import c.n.t.a.s.j.a.utils.BizzException;
import c.n.t.a.s.j.a.utils.MessageBundleUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.TimeZone;

/**
 * @ClassName Constants
 * @Author Colain.Yin
 * @date 2025/9/11 11:50
 */
@Slf4j
public class BaseTmsOpenApi {

    /**
     * The constant baseUrl.
     */
    private String baseUrl;
    /**
     * The constant apiKey.
     */
    private String apiKey;
    /**
     * The constant apiSecret.
     */
    private String[] apiSecret;

    public static int connectTimeout;
    public static int readTimeout;
    public static int retryTimes = 5;

    private TimeZone apiTimeZone = null;

    public BaseTmsOpenApi(String baseUrl, String apiKey, String apiSecret) {
    	if(baseUrl.endsWith("/")) {
    		baseUrl = baseUrl.substring(0, baseUrl.length()-1);
    	}
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.apiSecret = new String[1];
        this.apiSecret[0] = apiSecret;
        setDefaultTZ(null);
    }

    public BaseTmsOpenApi(String baseUrl, String apiKey, String localPvk, String peerPuk) {
        if(baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length()-1);
        }
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.apiSecret = new String[2];
        this.apiSecret[0] = localPvk;
        this.apiSecret[1] = peerPuk;
        setDefaultTZ(null);
    }


    private void setDefaultTZ(TimeZone timeZone){
        if(timeZone != null){
            apiTimeZone = timeZone;
        } else {
            apiTimeZone = TimeZone.getTimeZone("UTC");
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

    public String[] getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String[] apiSecret) {
        this.apiSecret = apiSecret;
    }

    protected SDKRequest createSdkRequest(String requestMappingUrl) {
        SDKRequest request = new SDKRequest(requestMappingUrl);
    	//request.addHeader("Time-Zone", apiTimeZone.getID());
    	return request;
    }

    protected SDKRequest createSdkRequest(String requestMappingUrl, boolean isUpload, String apkFilePath) {
        SDKRequest request = new SDKRequest(requestMappingUrl, isUpload, apkFilePath);
        //request.addHeader("Time-Zone", apiTimeZone.getID());
        return request;
    }


	protected SDKRequest getPageRequest(String requestUrl, PageRequest page) {
        SDKRequest request = createSdkRequest(requestUrl);
		request.addRequestParam("page", page);
		return request;
	}

	protected static String getMessage(String key) {
		return MessageBundleUtils.getMessage(key);
	}

	protected static String getMessage(String key, Object... args) {
		return MessageBundleUtils.getMessage(key, args);
	}



    public void setSDKConnectTimeout(int connectTimeout) throws BizzException {
        if(connectTimeout<0) {
            throw new BizzException("timeout can not be negative");
        }
        BaseTmsOpenApi.connectTimeout = connectTimeout;
    }

    public void setSDKReadTimeout(int readTimeout) throws BizzException {
        if(readTimeout<0) {
            throw new BizzException("timeout can not be negative");
        }
        BaseTmsOpenApi.readTimeout = readTimeout;
    }

    public void setRetryTimes(int retryTimes) throws BizzException {
        if(retryTimes <1 || retryTimes>5) {
            throw new BizzException("retryTimes cannot less than 0 and grate than 5");
        }
        BaseTmsOpenApi.retryTimes = retryTimes;
    }

    public enum SearchOrderBy {
        CreatedDate_desc("a.created_date DESC"),
        CreatedDate_asc("a.created_date ASC");
        private String val;
        private SearchOrderBy(String orderBy) {
            this.val = orderBy;
        }
        public String val(){
            return this.val;
        }
    }

    public enum PushStatus {
        Active("A"),
        Suspend("S");
        private String val;
        private PushStatus(String status) {
            this.val = status;
        }
        public String val() {
            return this.val;
        }
    }

}
