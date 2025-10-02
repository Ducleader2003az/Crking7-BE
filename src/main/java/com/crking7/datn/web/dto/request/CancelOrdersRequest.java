package com.crking7.datn.web.dto.request;

import lombok.Data;

@Data
public class CancelOrdersRequest {

    private Long orderId;

    private String note;

}
