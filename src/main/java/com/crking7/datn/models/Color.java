package com.crking7.datn.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "color")
public class Color {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String value;

	@ManyToOne()
	@JoinColumn(name = "product_id")
	private Product product;

	@OneToMany(mappedBy = "color", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Size> sizes = new ArrayList<>();
}
