package c.n.t.a.s.j.a.base.dto;

import lombok.Data;

/**
 * @ClassName PageRequest
 * @Author Colain.Yin
 * @date 2025/10/17 16:13
 */
@Data
public class PageRequest {
    /**
     * 当前游标
     */
    private Integer cur;
    /**
     * 页条数
     */
    private Integer size;
    /**
     * 总数
     */
    private Integer total;

    private String  orderBy;
}
