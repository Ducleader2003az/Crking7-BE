package com.crking7.datn.web.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ArticleRequest {
    private String title;
    private String shortContent;
    private String content;
    private String author;
    private Long userId;
    private Long categoryId;
    private String image;
}
