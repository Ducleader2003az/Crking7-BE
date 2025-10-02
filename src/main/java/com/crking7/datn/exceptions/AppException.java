package com.crking7.datn.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class AppException {
    private String message;

    public AppException(String message) {
        this.message = message;
    }
}
