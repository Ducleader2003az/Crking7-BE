package com.crking7.datn.repositories;

import com.crking7.datn.models.Product;
import com.crking7.datn.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProduct(Product product);

    ProductImage findByUrlAndProductId(String url, long productId);
}
