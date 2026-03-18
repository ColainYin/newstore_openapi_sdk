package c.n.t.a.s.j.a;

import com.alibaba.fastjson.JSON;
import c.n.t.a.s.j.a.base.dto.R;
import c.n.t.a.s.j.a.base.dto.TableDataInfo;
import c.n.t.a.s.j.a.contants.Constants;
import c.n.t.a.s.j.a.term.TermApi;
import c.n.t.a.s.j.a.term.dto.TermDetailReq;
import c.n.t.a.s.j.a.term.dto.TermInstallAppListReq;
import c.n.t.a.s.j.a.term.dto.TermAppInfResp;
import c.n.t.a.s.j.a.term.dto.TerminalDetailResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

@Slf4j
public class OpenSdkApiTest {

    private static final Logger logger = LoggerFactory.getLogger(OpenSdkApiTest.class.getSimpleName());

    TermApi termApi;

    @Before
    public void init() {
        termApi = new TermApi(Constants.REMOTE_NP_DEV_URL, TestApiConstants.API_KEY_NS,
                TestApiConstants.API_SECRET_NS);
    }

    /**
     * 终端已安装应用列表
     */
    @org.junit.Test
    public void testInstalledAppListCall() {
        TermInstallAppListReq req = new TermInstallAppListReq();
        req.setSn("921000010191");
        //test 1k request body
        String source = buildSource();
        req.setOther(source);
        req.setPageNum(1);
        req.setPageSize(10);
        TableDataInfo<TermAppInfResp> dataInfo = termApi.installedAppListCall(req);
        System.out.println("dataInfo:" + JSON.toJSON(dataInfo));
    }

    /**
     * test 1k request body
     * @return
     */
    private static String buildSource() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 100; i++) {
            sb.append("test1ktest1k1");
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        System.out.println("sb.length:" + bytes.length);
        // 输出sb的字节大小
        return sb.toString();
    }

    /**
     * 终端详情
     */
    @org.junit.Test
    public void testTermDetailCall() {
        TermDetailReq req = new TermDetailReq();
        req.setSn("9210000101911");
        R<TerminalDetailResp> resp = termApi.termDetailCall(req);
        System.out.println("resp:" + JSON.toJSON(resp));
    }

}
