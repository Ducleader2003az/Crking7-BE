package com.crking7.datn.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;

@Data
public class PolicyImageDto {
	private long id;

	@NotEmpty
	private String img;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private int status;
}
