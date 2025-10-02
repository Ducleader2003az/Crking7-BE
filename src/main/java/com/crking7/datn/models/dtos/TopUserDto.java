package com.crking7.datn.models.dtos;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class TopUserDto {
	private Long id;

	private String username;

	private Long totalOrder;

	private Long totalIncome;

	public TopUserDto(Long id, String username, Long totalOrder, Long totalIncome) {
		this.id = id;
		this.username = username;
		this.totalOrder = totalOrder;
		this.totalIncome = totalIncome;
	}
}
