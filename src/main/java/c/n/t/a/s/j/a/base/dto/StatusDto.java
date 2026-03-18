package c.n.t.a.s.j.a.base.dto;

import lombok.Data;

/**
 *
 * @author Colain.Yin
 * @date 2025-10-18
 */
@Data
public class StatusDto {

    /**
     * 返回信息
     */
    private String message;
    /**
     * 错误码
     */
    private String errorcode;

}
