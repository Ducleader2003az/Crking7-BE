package com.crking7.datn.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "category_policy")
public class CategoryPolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_policy_id")
	private long id;

	@Column(name = "category_policy_name", nullable = false)
	private String name;
	
	@Column
	private Date createdDate;

	@Column
	private Date modifiedDate;

	@Column(nullable = false)
	private int status;

	@OneToMany(mappedBy = "categoryPolicy", cascade = CascadeType.ALL)
	private Set<Policy> policies = new HashSet<>();
}
