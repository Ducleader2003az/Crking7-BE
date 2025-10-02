package com.crking7.datn.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "policy_image")
public class PolicyImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "policy_image_id")
	private long id;
	
	@Column
	private String img;
	
	@Column
	private Date createdDate;
	
	@Column
	private Date modifiedDate;
	
	@Column
	private int status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_id", nullable = false)
	private Policy policy;

}
