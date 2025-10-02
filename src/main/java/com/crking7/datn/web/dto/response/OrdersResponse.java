package com.crking7.datn.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrdersResponse {
    private long id;
    private String codeOrders;
    private String userNameEmp;
    private String fullName;
    private String phone;
    private int shippingFee;
    private String note;
    private String addressDetail;
    private String province;
    private String district;
    private String wards;
    private Boolean isCheckout;
    private String paymentMethod;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date shipDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date orderDate;
    private int type;
    private int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date modifiedDate;
    private Long user;
    private List<OrderItemResponse> items;
}
