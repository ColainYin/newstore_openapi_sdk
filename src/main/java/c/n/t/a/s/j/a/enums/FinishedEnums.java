package c.n.t.a.s.j.a.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @ClassName: FinishedEnums
 * @Author: jh
 * @Date: 2025/12/4 15:15
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum FinishedEnums {
    // 1.SUCCESS  2.FAILURE  3.OFFLINE  4.TIMEOUT
    SUCCESS("1", "SUCCESS"),
    FAILURE("2", "FAILURE"),
    OFFLINE("3", "OFFLINE"),
    TIMEOUT("4", "TIMEOUT");

    private final String code;
    private final String desc;
}
