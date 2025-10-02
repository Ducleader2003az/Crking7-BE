package com.crking7.datn.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SaleRequest {
    private String name;

    private Integer discount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date endDate;

}