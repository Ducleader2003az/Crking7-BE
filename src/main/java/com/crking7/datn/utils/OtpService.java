package com.crking7.datn.utils;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    private final Map<String, String> otpMap = new ConcurrentHashMap<>(); // Lưu trữ tạm thời mã OTP và email

    public void storeOtp(String email, String otp) {
        otpMap.put(email, otp);
        System.out.println(otp);
    }

    public String getOtp(String email) {
        return otpMap.get(email);
    }

    public void removeOtp(String email) {
        otpMap.remove(email);
    }
}
