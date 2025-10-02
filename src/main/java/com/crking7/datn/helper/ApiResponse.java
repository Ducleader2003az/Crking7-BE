package com.crking7.datn.helper;

import lombok.Data;

@Data
public class ApiResponse {
    private int code;
    private Boolean status;
    private String message;
    private Object data;
//    private String content;


    public ApiResponse(int code, String message, Object data, String content) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
//        this.content = content;

    }

    public ApiResponse(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ApiResponse() {
        // TODO Auto-generated constructor stub
        super();
    }

    public static ApiResponse build(int code, boolean status ,String message, Object data)
    {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(code);
        apiResponse.setStatus(status);
        apiResponse.setData(data);
        apiResponse.setMessage(message);
        return apiResponse;
    }

//    public static ApiResponse builder(int code, boolean status ,String message, Object data, String content)
//    {
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(code);
//        apiResponse.setStatus(status);
//        apiResponse.setData(data);
//        apiResponse.setMessage(message);
//        return apiResponse;
//    }
}