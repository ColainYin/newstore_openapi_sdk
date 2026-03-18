package c.n.t.a.s.j.a.term.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TerminalDetailResp implements Serializable {

    private static final long serialVersionUID=1L;

    private String fid;
    /**
     * Physical Identifier，通过终端注册时上送数据生成
     */
    private String pid;
    private Long termId;

    /**
     * 机型
     */
    private String modelId;
    private String model;
    /**
     * 机型图标
     */
    private String modelIcon;
    /**
     * 厂商名
     */
    private String vendorName;
    /**
     * 厂商编号
     */
    private String vendorCode;
    private String vendorId;

    /**
     * 序列号
     */
    private String sn;
    /**
     * 自定义标签
     */
    private String reserved;
    private String cid;
    /**
     * 定制信息
     */
    private String custom;
    /**
     * 固件id
     */
    private String firmwareid;
    /**
     * 固件类型 0.正式版1.调试版
     */
    private String firmwareType;
    /**
     * 固件版本
     */
    private String firmwareVersion;
    /**
     * 固件名称
     */
    private String firmwareName;
    /**
     * 应用市场版本
     */
    private String appstoreVersion;
    /**
     * 是否允许更新
     */
    private String allowUpdate;
    /**
     * 锁定状态
     */
    private String locked;
    /**
     * 首次激活时间
     */
    private LocalDateTime activeTime;
    /**
     * 机构ID
     */
    private Long belongBranchId;
    /**
     * 机构名称
     */
    private String branchName;
    /**
     * 使用类型
     */
    private String usageType;
    /**
     * 是否支持iot 0-不支持  1-支持
     */
    private String mqttEnable;
    /**
     * 是否开启地理围栏，0-不开启，1-开启
     */
    private String geoFencing;
    /**
     * 是否自动锁机，0-手动锁机，1-自动锁机
     */
    private String autoLock;
    /**
     * 地理围栏经度
     */
    private String setLon;
    /**
     * 地理围栏纬度
     */
    private String setLat;
    /**
     * 地理围栏半径(单位:米)
     */
    private Integer setRadius;
    /**
     * 请求主机ID
     */
    private Integer requestHostId;
    /**
     * 地图类型
     */
    private String mapType;
    private String mapAk;

    /**
     * 附属信息
     */
    private TerminalExtraVo extra;
    /**
     * 静态数据
     */
    private Map staticData;
}
