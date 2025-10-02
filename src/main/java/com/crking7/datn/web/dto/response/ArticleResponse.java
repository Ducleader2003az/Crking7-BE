package com.crking7.datn.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ArticleResponse {
    private long id;
    private String title;
    private String shortContent;
    private String content;
    private String author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date modifiedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;
    private long userId;
    private long categoryId;
    private int status;
    private String image;
}
