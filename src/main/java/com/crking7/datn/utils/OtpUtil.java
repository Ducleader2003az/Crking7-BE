package com.crking7.datn.utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpUtil {

    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);

        while (output.length() < 6) {
            output = "0" + output;
        }
        return output;
    }
}
