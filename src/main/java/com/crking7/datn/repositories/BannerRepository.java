package com.crking7.datn.repositories;

import com.crking7.datn.models.Banner;
import com.crking7.datn.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    Page<Banner> findByCategoryAndStatus(Pageable pageable, Category category, int status);

    Page<Banner> findByStatus(Pageable pageable, int status);

    Banner findByName(String name);

    @Query("SELECT b FROM Banner b WHERE (:keyword IS NULL OR b.name LIKE %:keyword%) " +
            "AND (:status IS NULL OR b.status = :status)"
    )
    Page<Banner> findAllBanners(@Param("keyword") String keyword,
                                @Param("status") Integer status,
                                Pageable pageable);
}
