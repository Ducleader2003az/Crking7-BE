package com.crking7.datn.web.dto.request;

import lombok.Data;

@Data
public class AddressRequest {
    private String firstName;

    private String lastName;

    private String phone;

    private String addressDetail;

    private String province;

    private String district;

    private String wards;

    private Long userId;
}
