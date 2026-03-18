package c.n.t.a.s.j.a.term;

import c.n.t.a.s.j.a.term.dto.TermAppInfResp;
import c.n.t.a.s.j.a.term.dto.TermDetailReq;
import c.n.t.a.s.j.a.term.dto.TermInstallAppListReq;
import c.n.t.a.s.j.a.term.dto.TerminalDetailResp;
import com.alibaba.fastjson.JSON;
import c.n.t.a.s.j.a.BaseTmsOpenApi;
import c.n.t.a.s.j.a.base.dto.R;
import c.n.t.a.s.j.a.base.dto.TableDataInfo;
import c.n.t.a.s.j.a.base.request.SDKRequest;
import c.n.t.a.s.j.a.client.OpenApiClient;
import c.n.t.a.s.j.a.contants.Constants;
import c.n.t.a.s.j.a.utils.EnhancedJsonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName LogsApi
 * @Author Colain.Yin
 * @date 2025/11/10 16:00
 */
@Slf4j
public class TermApi extends BaseTmsOpenApi {
    protected static final String INSTALLED_APP_LIST_CALL_URL = "/sdkApi.queryInstalledAppList";
    protected static final String TERM_DETAIL_CALL_URL = "/sdkApi.queryTermInfo";


    public TermApi(String baseUrl, String apiKey, String apiSecret) {
        super(baseUrl, apiKey, apiSecret);
    }

    public TermApi(String baseUrl, String apiKey, String localPvk, String peerPuk) {
        super(baseUrl, apiKey, localPvk, peerPuk);
    }

    /**
     *
     * @param req
     * @return
     */
    public TableDataInfo<TermAppInfResp> installedAppListCall(TermInstallAppListReq req) {
        OpenApiClient client = new OpenApiClient(getBaseUrl(), getApiKey(), getApiSecret());

        SDKRequest request = createSdkRequest(INSTALLED_APP_LIST_CALL_URL);
        request.setRequestMethod(SDKRequest.RequestMethod.POST);
        request.setRequestBody(JSON.toJSONString(req));
        request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
        request.addHeader(Constants.REQ_HEADER_SDK_AUTHSN_NS, req.getSn());
        TableDataInfo<TermAppInfResp> resp = EnhancedJsonUtils.fromJson(client.execute(request), TableDataInfo.class);
        return resp;
    }

    /**
     * req
     * @param req
     * @return
     */
    public R<TerminalDetailResp> termDetailCall(TermDetailReq req) {
        OpenApiClient client = new OpenApiClient(getBaseUrl(), getApiKey(), getApiSecret());

        SDKRequest request = createSdkRequest(TERM_DETAIL_CALL_URL);
        request.setRequestMethod(SDKRequest.RequestMethod.POST);
        request.setRequestBody(JSON.toJSONString(req));
        request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
        request.addHeader(Constants.REQ_HEADER_SDK_AUTHSN_NS, req.getSn());
        R<TerminalDetailResp> resp = EnhancedJsonUtils.fromJson(client.execute(request), R.class);
        return resp;
    }


}
