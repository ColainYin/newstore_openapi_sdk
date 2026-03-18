package c.n.t.a.s.j.a.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author lh
 */
@Data
@NoArgsConstructor
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final String SUCCESS = "200";

    /**
     * 失败
     */
    public static final String FAIL = "500";

    /**
     * 参数校验失败
     */
    public static final String FAIL_PARAM = "400";

    private String code;

    private String msg;

    private T data;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, "Operation successful");
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, "Operation successful");
    }

    public static <T> R<T> ok(String msg) {
        return restResult(null, SUCCESS, msg);
    }

    public static <T> R<T> ok(String msg, T data) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL, "Operation failed");
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, "Operation failed");
    }

    public static <T> R<T> failByParamsValid(T data) {
        return restResult(data, FAIL_PARAM, "参数异常");
    }

    public static <T> R<T> fail(String msg, T data) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(String code, String msg) {
        return restResult(null, code, msg);
    }


    private static <T> R<T> restResult(T data, String code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    public static <T> Boolean isError(R<T> ret) {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(R<T> ret) {
        return R.SUCCESS == ret.getCode();
    }
}
