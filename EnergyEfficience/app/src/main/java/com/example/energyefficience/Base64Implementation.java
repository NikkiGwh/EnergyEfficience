package com.example.energyefficience;

import java.util.Base64;

public class Base64Implementation {
    public static String encodeSynchronously(String plainText){
        String result = "";
        byte[] buffer = plainText.getBytes();
        result = Base64.getEncoder().encodeToString(buffer);
        return result;
    }

    public static String createBigString(int msgSize){
        //java chars are 2 bytes --> /2 = anzahl der chars * 1024 = Anzahl der Zahl für n KB
        msgSize = msgSize / 2;
        msgSize = msgSize* 1024;
        StringBuilder sb = new StringBuilder(msgSize);
        for(int i = 0; i< msgSize; i++){
            sb.append('a');
        }
        return sb.toString();
    }
}
