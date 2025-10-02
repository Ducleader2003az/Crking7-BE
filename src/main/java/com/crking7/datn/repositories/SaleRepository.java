package com.crking7.datn.repositories;

import com.crking7.datn.models.Banner;
import com.crking7.datn.models.Color;
import com.crking7.datn.models.Sale;
import com.crking7.datn.models.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    Sale findByName(String name);

    List<Sale> findByIsActive(Integer isActive);

    @Query("SELECT s FROM Sale s WHERE (:keyword IS NULL OR s.name LIKE %:keyword%) " +
            "AND (:isActive IS NULL OR s.isActive = :isActive)"
    )
    Page<Sale> findAllSale(@Param("keyword") String keyword,
                           @Param("isActive") Integer isActive,
                           Pageable pageable);
}
