package c.n.t.a.s.j.a.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Colain.Yin
 * @date 2025-10-18
 */
@Data
public class BaseDTO implements Serializable {
    /**
     * 自定义表单参数
     */
    private Map<String, Object> options;

    private String token;
}
