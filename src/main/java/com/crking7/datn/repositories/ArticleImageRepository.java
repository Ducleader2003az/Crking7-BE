package com.crking7.datn.repositories;

import com.crking7.datn.models.Article;
import com.crking7.datn.models.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
	List<ArticleImage> findAllByArticle(Article article);
}

