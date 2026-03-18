package c.n.t.a.s.j.a.term.dto;

import java.io.Serializable;

/**
 * 卸载应用请求参数
 * @author C26
 */
public class TermUninstallAppReq implements Serializable {

    /**
     * {
     *     "distributionId": "TM1325495211",
     *     "applicationId": "jp.co.tance.sample",
     *     "requestId": "ADTDjsp-oACT-CNvI29RFo7pg",
     *     "businessId": "T5498821",
     *     "tanceAccount": "sample@gmail.com"
     * }
     */
    private String distributionId;
    private String applicationId;
    private String requestId;
    private String businessId;
    private String tanceAccount;
}
