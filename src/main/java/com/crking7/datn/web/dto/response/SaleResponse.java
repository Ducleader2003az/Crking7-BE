package com.crking7.datn.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SaleResponse {
    private Long id;

    private String name;

    private Integer discount;

    private Integer isActive;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date modifiedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

}
