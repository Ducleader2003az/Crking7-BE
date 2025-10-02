package com.crking7.datn.web.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private int id;
    private int quantity;
    private int sellPrice;
    private String productName;
    private String valueColor;
    private String valueSize;
    private int maxQuantity;
}
