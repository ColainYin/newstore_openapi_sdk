package c.n.t.a.s.j.a.term.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 *
 * @author C26
 */
@Data
public class TerminalExtraVo implements Serializable {

    /**
     * 终端业务id
     */
    private String fid;
    /**
     * 机构id
     */
    private Long branchId;
    /**
     * 1：MQTT心跳
     */
    private String heartType;
    /**
     * POS随机DES密钥
     */
    private String termKey;
    /**
     * 经度/纬度
     */
    private String labinfo;
    /**
     * 终端地址
     */
    private String address;
    /**
     * 基站信息
     */
    private String cellinfo;

    /**
     * MCC 移动国家代码
     */
    private Integer mcc;
    /**
     * MNC 移动网络代码
     */
    private Integer mnc;
    /**
     * CELLID 基站编号
     */
    private Integer cellid;
    /**
     * ICCID SIM卡卡号
     */
    private String iccid;
    /**
     * 国际移动设备识别码（IMEI, International Mobile Station Equipment Identity)
     */
    private String imei;
    private String meid;
    private String imei2;
    private String meid2;
    /**
     * 终端锁定状态，1-锁定,0-解锁
     */
    private String locked;
    /**
     * WiFiMAC地址
     */
    private String macWlan;
    /**
     * 蓝牙MAC地址
     */
    private String macBlu;
    /**
     * 语言
     */
    private String language;
    /**
     * 时区
     */
    private String timeZone;
    /**
     * 上次上线时设备IP
     */
    private String ip;
    /**
     * 上线状态，0:offline, 1:online
     */
    private String status;

    /**
     * 上次上线时间
     */
    private LocalDateTime loginTime;

    /**
     * 网络类型，0-Not_Connected(未连接网络)，1-WLAN(无线网)，2-2G（2G网络），3-3G（3G网络），4-4G（4G网络），5-ETH（LAN）以太网
     */
    private String netType;

    /**
     * 信号格数，1-5
     */
    private Integer dbm;

    /**
     * 电池状态，1-Charging（充电中），2-Discharging（未充电），3-Filled（已冲满）
     */
    private String charge;
    /**
     * 当前电池剩余百分比，0~100
     */
    private String battery;
    /**
     * 电池健康状态，1-良好Good，2-过热OverHeat, 3-Dead，4-过压Over Voltage，5-未指定的错误Upspecified，6-低温Cold, 7-未知
     */
    private String batteryHealth;
    /**
     * 当前CPU使用百分比，0~100
     */
    private String cpu;
    /**
     * 当月WIFI使用流量（单位：字节）
     */
    private BigInteger wifi;
    /**
     * 当月Cellular使用流量（单位：字节）
     */
    private BigInteger cellular;
    /**
     * 当前RAM使用大小
     */
    private BigInteger ramUsed;
    /**
     * 当前RAM总容量大小
     */
    private BigInteger ramTotal;
    /**
     * 当前Storage使用大小
     */
    private BigInteger storageUsed;
    /**
     * 当前Storage总容量大小
     */
    private BigInteger storageTotal;
    /**
     * 攻击状态 0-正常，1-攻击
     */
    private Integer pedStatus;
    /**
     * 超出围栏次数
     */
    private Integer outFencing;
    /**
     * 已开机时间(开机到现在的运行秒数)
     */
    private Long runningTime;
    /**
     * 同步状态时间
     */
    private LocalDateTime syncTime;
}
