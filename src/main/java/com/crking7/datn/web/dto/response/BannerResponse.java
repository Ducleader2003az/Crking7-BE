package com.crking7.datn.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;

@Data
public class BannerResponse {
	private long id;

	private String name;

	private String src;

	private int status;

	private long categoryId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date modifiedDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date createdDate;
}
