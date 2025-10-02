package com.crking7.datn.models;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Table(name = "product",uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })

public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column
	private String sku;

	@Column
	private String material;

	@Column
	private int visited;

	@Column
	private int price;

	@Column
	private int salePrice;

	@Column
	private Date modifiedDate;

	@Column
	private Date createdDate;

	@Column
	private int status;

	@ManyToOne
	@JoinColumn(name = "product_author_id")
	private User productAuthor;

	@ManyToOne
	@JoinColumn(name = "product_category_id")
	private Category productCategory;

	@ManyToOne
	@JoinColumn(name = "sale_id")
	private Sale sale;

	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Color> colors = new ArrayList<>();

	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<ProductImage> productImages = new ArrayList<>();

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Notification> notifications = new ArrayList<>();
}
