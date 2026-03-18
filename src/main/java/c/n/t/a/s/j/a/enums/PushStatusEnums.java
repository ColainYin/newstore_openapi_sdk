package c.n.t.a.s.j.a.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @ClassName: PushStatusEnums
 * @Author: jh
 * @Date: 2025/12/4 15:12
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum PushStatusEnums {
    //"0":disabled, "1":not started, "2":in progress, "3":ended
    DISABLED("0","Disabled"),
    NOT_STARTED("1" ,"Not Started"),
    IN_PROGRESS("2" ,"In Progress"),
    ENDED("3" ,"Ended");

    private final String code;
    private final String desc;
}
