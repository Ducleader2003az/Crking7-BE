package com.crking7.datn.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class UserResponse {
    private long id;

    private String username;

    private String email;

    private String otp;

    private String firstName;

    private String lastName;

    private String phone;

    private String image;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date modifiedDate;

    private Set<RoleResponse> roles;

    private int status;

    private List<AddressResponse> addresses;

}
