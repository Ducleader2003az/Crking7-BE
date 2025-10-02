package com.crking7.datn.web.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductUDRequest {
    private long id;

    private String name;

    private String description;

    private String material;

    private int price;

    private Long categoryId;

    private Long userId;

    private List<ColorUDRequest> colors;

    private List<ProductImageUDRequest> images;
}
