	package com.crking7.datn.models;

	import com.fasterxml.jackson.annotation.JsonBackReference;
	import com.fasterxml.jackson.annotation.JsonManagedReference;
	import jakarta.persistence.*;
	import lombok.AllArgsConstructor;
	import lombok.Getter;
	import lombok.NoArgsConstructor;
	import lombok.Setter;

	import java.util.*;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Entity
	@Table(name = "category")
	public class Category {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column
		private long id;

		@Column(nullable = false)
		private String title;

		@Column
		private String description;

		@Column
		private Date createdDate;

		@Column
		private Date modifiedDate;

		@Column
		private String urlImage;

		@Column
		private int type;//xác định là 0: sản phẩm, 1: chính sách, 2: bài viết, 3: tuyển dụng

		@Column(nullable = false)
		private int status;//xác định là 0: ẩn , 1: hiện

		@ManyToOne
		@JoinColumn(name = "parent_category_id")
		@JsonBackReference
		private Category parentCategory;

		@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
		@JsonManagedReference
		private List<Article> articles = new ArrayList<>();

		@OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
		@JsonManagedReference
		private List<Category> childCategories = new ArrayList<>();

		@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
		@JsonManagedReference
		private Set<Banner> banners = new HashSet<>();

		@OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
		@JsonManagedReference
		private List<Product> products = new ArrayList<>();
	}