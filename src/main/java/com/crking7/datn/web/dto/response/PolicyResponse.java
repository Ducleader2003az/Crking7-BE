package com.crking7.datn.web.dto.response;

import com.crking7.datn.models.dtos.PolicyDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyResponse {
	
	private String name;
	
	private List<PolicyDto> content;
	
	private int pageNo;
	
	private int pageSize;
	
	private long totalElements;
	
	private int totalPages;
	
	private boolean Last;
}
