package com.shopping.portal.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResponseApi {

	private String timestamp;
	private int status;
	private String statusdescription;
	private String message;
	private List<Object> data;

	public static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public ResponseApi(int status, String statusdescription, String message, List<Object> data) {
		super();
		this.timestamp = sf.format(new Date());
		this.status = status;
		this.statusdescription = statusdescription;
		this.message = message;
		this.data = data;
	}

	public static ResponseApi createResponse(int status, String message, List<Object> data) {
		String statusDescription = (status == 1) ? "Success" : "Fail";
		return new ResponseApi(status, statusDescription, message, data);
	}

}
