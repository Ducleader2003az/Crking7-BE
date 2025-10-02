package com.crking7.datn.web.dto.request;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {
    private String name;

    private String description;

    private String material;

    private int price;

    private Long categoryId;

    private Long userId;

    private List<ColorRequest> colors;

    private List<ProductImageRequest> images;
}
