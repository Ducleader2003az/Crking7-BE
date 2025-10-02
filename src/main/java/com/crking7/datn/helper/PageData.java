package com.crking7.datn.helper;

import lombok.Data;

import java.util.List;

@Data
public class PageData {
    private int currentPage;
    private List<Object> data;
    private int perPage;
    private int total;

}
