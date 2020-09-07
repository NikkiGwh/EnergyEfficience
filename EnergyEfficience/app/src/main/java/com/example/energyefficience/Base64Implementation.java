package com.example.energyefficience;

import java.util.Base64;

public class Base64Implementation {
    public static String encodeOnUIThread(String plainText){
        String result = "";
        byte[] buffer = plainText.getBytes();
        result = Base64.getEncoder().encodeToString(buffer);
        return result;
    }
}
