package c.n.t.a.s.j.a.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Colain.Yin
 * @date 2025-10-18
 */
@Data
public class Response<T extends Serializable> extends BaseDTO {
	
	private static final long serialVersionUID = -654338248679789942L;

	public StatusDto status;

	private T data;
	
}
