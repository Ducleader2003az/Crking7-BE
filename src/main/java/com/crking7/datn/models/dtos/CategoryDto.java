package com.crking7.datn.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class CategoryDto {
	private long id;
	
	@NotEmpty
	@Size(min =2, message = "Category title should have at least 2 characters")
	private String title;
	@NotEmpty
	@Size(min =2, message = "Category description should have at least 10 characters")
	private String description;

	private String urlImage;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private int status;
	
//	private Set<BannerDto> banners;
}
