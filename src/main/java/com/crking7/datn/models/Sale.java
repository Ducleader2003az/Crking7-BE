package com.crking7.datn.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sales")
public class Sale {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String name;

	@Column
	private Integer discount;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	@Column
	private Date startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	@Column
	private Date endDate;

	@Column
	private Integer isActive; //0 là không hoạt động, 1 là hoạt động

	@Column
	private Date modifiedDate;

	@Column
	private Date createdDate;

	@OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Product> products = new ArrayList<>();
}
