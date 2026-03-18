package c.n.t.a.s.j.a.term.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 终端安装应用vo
 *
 * @author C26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TermAppInfResp implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;
    private Long fid;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 程序名称
     */
    private String name;
    /**
     * 应用版本号
     */
    private String vercode;
    /**
     * 应用版本名称
     */
    private String vername;
    /**
     * 应用来源 0-System APP, 1-APPSTORE,2-ThirdParty
     * @see AppSourceEnum
     */
    private String source;
    /**
     * 应用大小
     */
    private Long appSize;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 当月流量(cellular)
     */
    private String cellular;
    /**
     * 当月流量（wifi）
     */
    private String wifi;

    private String iconFile;

    /**
     * 是否可卸载，为空或者0则可卸载，1-不可卸载
     */
    private String canUninstall;
}
