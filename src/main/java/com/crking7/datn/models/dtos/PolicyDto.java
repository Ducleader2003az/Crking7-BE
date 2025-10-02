package com.crking7.datn.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class PolicyDto {
	private long id;
	@NotEmpty
	@Size(min =2, message = "Policy name should have at least 2 characters")
	private String name;
	@NotEmpty
	@Size(min = 10, message = "Policy content should have at least 10 characters")
	private String content;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private int status;
}
