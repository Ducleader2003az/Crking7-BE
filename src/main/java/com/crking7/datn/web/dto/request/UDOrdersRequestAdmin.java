package com.crking7.datn.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.util.Date;

@Data
public class UDOrdersRequestAdmin {
    private Long orderId;

    private String userNameEmp;

    private String fullName;

    private String phone;

    private String note;

    private String addressDetail;

    private String province;

    private String district;

    private String wards;

}
