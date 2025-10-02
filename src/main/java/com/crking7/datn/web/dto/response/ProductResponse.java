package com.crking7.datn.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ProductResponse {
    private Long id;

    private String name;

    private String description;

    private String sku;

    private String material;

    private int visited;

    private int price;

    private int salePrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date modifiedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    private int status;

    private long author;

    private long category;

    private long sale;

    private List<ColorResponse> colors;

    private List<ProductImageResponse> images;
}
