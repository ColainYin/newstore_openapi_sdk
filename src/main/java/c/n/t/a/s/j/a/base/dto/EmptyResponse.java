package c.n.t.a.s.j.a.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Colain.Yin
 * @date 2025-10-18
 */
@Data
public class EmptyResponse implements Serializable {

	private static final long serialVersionUID = 6604614621742021927L;
	public StatusDto status;
}
