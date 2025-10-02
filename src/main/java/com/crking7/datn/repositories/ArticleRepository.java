package com.crking7.datn.repositories;

import com.crking7.datn.models.Category;
import com.crking7.datn.models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findByIdAndStatus(long articleId, int status);

    Page<Article> findAllByStatus(Pageable pageable, int status);

    Page<Article> findAllByCategoryAndStatus(Pageable pageable, Category category, int status);

    Article findByTitle(String title);

    @Query("SELECT a FROM Article a WHERE (:keyword IS NULL OR a.title LIKE %:keyword%) " +
            ("AND :status IS NULL OR a.status = :status")
    )
    Page<Article> findAllByKeyword(@Param("keyword") String keyword,
                                   @Param("status") Integer status,
                                   Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.status = 1 ORDER BY a.id DESC")
    List<Article> findByNewest();

    @Query("SELECT a FROM Article a WHERE a.category.id = :categoryId AND a.status = 1 ORDER BY RAND() LIMIT :limit")
    List<Article> findRelatedArticle(@Param("categoryId") Long categoryId, @Param("limit") int limit);
}
