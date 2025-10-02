package com.crking7.datn.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class BannerDto {
	private long id;
	@NotEmpty
	@Size(min =2, message = "Banner name should have at least 2 characters")
	private String name;
	@NotEmpty
	private String src;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private int status;
}
