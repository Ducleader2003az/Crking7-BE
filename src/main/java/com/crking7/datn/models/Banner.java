package com.crking7.datn.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "banner")
public class Banner {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column
	private String src;
	
	@Column
	private Date createdDate;
	
	@Column
	private Date modifiedDate;

	@Column(nullable = false)
	private int status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	@JsonBackReference
	private Category category;

}
