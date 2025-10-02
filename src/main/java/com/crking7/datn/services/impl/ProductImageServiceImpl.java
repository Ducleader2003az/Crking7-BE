package com.crking7.datn.services.impl;

import com.crking7.datn.mapper.ProductImageMapper;
import com.crking7.datn.mapper.SizeMapper;
import com.crking7.datn.models.Product;
import com.crking7.datn.models.ProductImage;
import com.crking7.datn.models.dtos.SizeDto;
import com.crking7.datn.repositories.ProductImageRepository;
import com.crking7.datn.repositories.SizeRepository;
import com.crking7.datn.services.ProductImageService;
import com.crking7.datn.services.SizeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductImageMapper productImageMapper;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository,
                                   ProductImageMapper productImageMapper) {
        this.productImageRepository = productImageRepository;
        this.productImageMapper = productImageMapper;
    }

    @Override
    public void delete(long id) {
        ProductImage productImage = productImageRepository.findById(id).orElseThrow();
        productImage.setProduct(null);
        productImageRepository.save(productImage);
        productImageRepository.delete(productImage);
    }
}
