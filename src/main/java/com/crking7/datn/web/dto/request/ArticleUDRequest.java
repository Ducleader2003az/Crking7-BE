package com.crking7.datn.web.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ArticleUDRequest {
    private String title;
    private String shortContent;
    private String content;
    private String author;
    private long userId;
    private long categoryId;
    private String images;
}
