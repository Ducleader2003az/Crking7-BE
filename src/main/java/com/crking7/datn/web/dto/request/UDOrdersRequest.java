package com.crking7.datn.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.util.Date;

@Data
public class UDOrdersRequest {

    private Long orderId;

    private String userNameEmp;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date shipDate;
}
