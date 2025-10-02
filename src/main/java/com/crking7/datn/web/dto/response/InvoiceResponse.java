package com.crking7.datn.web.dto.response;

import com.crking7.datn.web.dto.request.OrderItemRequest;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvoiceResponse {
    private String code;

    private String fullName;

    private String phone;

    private String note;

    private String addressDetail;

    private String province;

    private String district;

    private String wards;

    private String paymentMethod;

    private Integer expireTime;

    private List<OrderItemRequest> items;
}
