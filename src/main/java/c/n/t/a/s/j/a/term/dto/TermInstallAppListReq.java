package c.n.t.a.s.j.a.term.dto;

import c.n.t.a.s.j.a.base.dto.PageQuery;
import lombok.Data;

import java.io.Serializable;

@Data
public class TermInstallAppListReq implements Serializable {

    private String sn;
    private String name;
    private String source;
    private String other;

    /**
     * 分页大小
     */
    private Integer pageSize = PageQuery.DEFAULT_PAGE_SIZE;

    /**
     * 当前页数
     */
    private Integer pageNum = PageQuery.DEFAULT_PAGE_NUM;

}
