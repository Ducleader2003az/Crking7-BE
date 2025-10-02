package com.crking7.datn.utils;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

public class PasswordEncoderGenerator {
    public static void main(String[] args){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123456"));

        Date date = new Date();
        System.out.println(date);
    }
}

