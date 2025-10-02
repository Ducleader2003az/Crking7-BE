package com.crking7.datn.web.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AddInvoiceRequest {
    private String code;

    private String fullName;

    private String phone;

    private String note;

    private String addressDetail;

    private String province;

    private String district;

    private String wards;

    private String paymentMethod;

    private List<OrderItemRequest> items;
}
