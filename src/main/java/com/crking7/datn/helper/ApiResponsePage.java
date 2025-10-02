package com.crking7.datn.helper;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponsePage {
    private Boolean status;
    private int code;
    private String message;
//    private String content;
    private PageData data;

    public ApiResponsePage(int code, String message, PageData data, String content) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
//        this.content = content;
    }

    public ApiResponsePage(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ApiResponsePage() {
        // TODO Auto-generated constructor stub
        super();
    }

    public static ApiResponsePage build(int code, boolean status,int currentPage, int perPage, int total, String message, List<Object> data)
    {
        ApiResponsePage apiResponse = new ApiResponsePage();
        PageData pageData = new PageData();
        pageData.setCurrentPage(currentPage);
        pageData.setPerPage(perPage);
        pageData.setTotal(total);
        pageData.setData(data);
        apiResponse.setStatus(status);
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        apiResponse.setData(pageData);

        return apiResponse;
    }
    public static ApiResponsePage builder(int code, boolean status, String message, List<Object> data)
    {
        ApiResponsePage apiResponse = new ApiResponsePage();
        PageData pageData = new PageData();
        pageData.setData(data);
        apiResponse.setStatus(status);
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        apiResponse.setData(pageData);
//        apiResponse.setContent(content);
        return apiResponse;
    }
}
