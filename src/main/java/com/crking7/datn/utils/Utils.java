package com.crking7.datn.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Utils {
    public String generateRandomCharacters(int length) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String allLetters = upperCaseLetters + lowerCaseLetters;

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allLetters.length());
            char randomChar = allLetters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
    public String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder newPassword = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            newPassword.append(randomChar);
        }

        return newPassword.toString();
    }
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
