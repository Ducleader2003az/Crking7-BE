package com.crking7.datn.models;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="article_image")
public class ArticleImage{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String url;
	
	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;
}

