package c.n.t.a.s.j.a.term.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 卸载应用响应参数
 * @author C26
 */
@Data
public class TermUninstallAppResp implements Serializable {

    /**
     * "requestId":  "ADTDjsp-oACT-CNvI29RFo7pg",
     *     "message":  "指定した配信先(TM1325495211)が見つかりません",
     *     "errorInfo":  "[ERROR CODE:YX120001] terminal(terminalId:TM1325495211) was not found"
     */
    private String requestId;

    private String message;

    private String errorInfo;
}
