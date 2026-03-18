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
public class Result<T extends Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6318528536528828759L;
	
	private StatusDto status;
	private T data;

	private PageResponse<T> pageResponse;
	
	public Result() {
		
	}
	
	public Result(List<String> errors) {
		StatusDto statusDto = new StatusDto();
		statusDto.setErrorcode("00");
		statusDto.setMessage("success");
		this.setStatus(statusDto);
	}
	
	public Result(Response<T> response) {
		this.setStatus(response.getStatus());
		this.data = response.getData();
	}
	
	public Result(PageResponse<T> response) {
		PageResponse<T> pageInfo = new PageResponse<T>();
		pageInfo.setDataList(response.getDataList());
		pageInfo.setPage(response.getPage());
		this.setStatus(response.getStatus());
		this.pageResponse = pageInfo;
	}

	public Result(EmptyResponse response) {
		if (response == null) {
			StatusDto statusDto = new StatusDto();
			statusDto.setErrorcode("00");
			statusDto.setMessage("success");
			this.setStatus(statusDto);
		} else {
			setStatus(response.getStatus());
			this.data = null;
		}

	}

}
