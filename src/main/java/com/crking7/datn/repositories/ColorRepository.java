package com.crking7.datn.repositories;

import com.crking7.datn.models.Color;
import com.crking7.datn.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {

    Color findByValueAndProductId(String value, long productId);

    List<Color> findByProduct(Product product);

    @Query("SELECT DISTINCT c.value FROM Color c")
    List<String> findDistinctColorValues();
}
