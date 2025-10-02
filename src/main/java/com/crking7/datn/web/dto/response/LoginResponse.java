package com.crking7.datn.web.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;

    private UserResponse user;
}
