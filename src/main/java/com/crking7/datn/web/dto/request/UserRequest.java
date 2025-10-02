package com.crking7.datn.web.dto.request;


import lombok.Data;

@Data
public class UserRequest {

    private String email;

    private String firstName;

    private String lastName;

    private String phone;
}
