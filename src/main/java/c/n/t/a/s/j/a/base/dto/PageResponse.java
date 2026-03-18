package c.n.t.a.s.j.a.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Colain.Yin
 * @date 2025-10-18
 */
@Data
public class PageResponse<T extends Serializable> implements Serializable{
	private static final long serialVersionUID = -2620822481165409572L;
	private PageRequest page;
    private List<T> dataList;
	public StatusDto status;
	
}
