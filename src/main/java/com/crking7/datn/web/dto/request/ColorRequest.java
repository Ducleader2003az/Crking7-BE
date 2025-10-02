package com.crking7.datn.web.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ColorRequest {
    private String value;

    private List<SizeRequest> sizes;

    private long productId;
}
