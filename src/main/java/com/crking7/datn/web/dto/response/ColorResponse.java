package com.crking7.datn.web.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ColorResponse {
    private Long id;

    private String value;

    private List<SizeResponse> sizes;
}
