package com.crking7.datn.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="article")
public class Article {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String title;

	@Column(columnDefinition = "TEXT")
	private String shortContent;

	@Column(columnDefinition = "TEXT")
	@Lob
	private String content;

	@Column
	private String author;
	
	@Column
	private Date createdDate;
	
	@Column
	private Date modifiedDate;

	@Column
	private String image;

	@Column
	private int status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

}

