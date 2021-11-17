package com.spring.bank;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

public class HelperUtil {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String base64(String encoder){
        return Base64.getEncoder().encodeToString(encoder.getBytes());
    }
}