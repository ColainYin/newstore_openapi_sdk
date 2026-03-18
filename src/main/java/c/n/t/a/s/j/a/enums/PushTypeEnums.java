package c.n.t.a.s.j.a.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @ClassName: PushTypeEnum
 * @Author: jh
 * @Date: 2025/12/3 15:16
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum PushTypeEnums {
    SYSTEM_APK_COLLECT("12-1","system layer apk collection"),
    STOP_SYSTEM_APK_COLLECT("12-0","stop system layer apk collection"),
    PUSH_ONE_APK_INSTALL("09-1","push one apk install"),
    PUSH_ONE_APK_UNINSTALL("10-1","push one apk uninstall"),
    OTA_UPGRADE_NOTIFY("05-1","ota upgrade notification"),
    APPLICATION_APK_COLLECT("11-1","application layer apk collection"),
    STOP_APPLICATION_APK_COLLECT("11-0","stop application layer apk collection"),
    ;
    private final String code;
    private final String desc;
}
