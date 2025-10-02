package com.crking7.datn.web.dto.request;

import lombok.Data;


@Data
public class OrdersRequest {
    private String fullName;

    private String phone;

    private String note;

    private String addressDetail;

    private String province;

    private String district;

    private String wards;

}
