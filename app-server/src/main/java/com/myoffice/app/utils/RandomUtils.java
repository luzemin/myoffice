package com.myoffice.app.utils;

import java.util.Random;

public class RandomUtils {
    public static String code(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 5;
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomString.append(randomChar);
        }
        return randomString.toString();
    }
}
