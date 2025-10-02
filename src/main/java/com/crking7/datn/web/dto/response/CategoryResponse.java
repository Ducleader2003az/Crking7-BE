package com.crking7.datn.web.dto.response;
import com.crking7.datn.models.Banner;
import com.crking7.datn.models.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CategoryResponse {
	private long id;

	private String title;

	private  int status;

	private String description;

	private String urlImage;

	private int type;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date modifiedDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date createdDate;

	private Set<Banner> banners = new HashSet<>();

	private Long categoryParent;

	private List<CategoryResponse> childCategories = new ArrayList<>();

	public void setParentCategory(Category parentCategory) {
		// Không làm gì trong phương thức này
	}
}
