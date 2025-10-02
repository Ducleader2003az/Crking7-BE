package com.crking7.datn.web.dto.response;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class AddressResponse {
    private long id;

    private String firstName;

    private String lastName;

    private String phone;

    private String addressDetail;

    private String province;

    private String district;

    private String wards;

    private Date createdDate;

    private Date modifiedDate;

    private int focus;

    private int status;
}
