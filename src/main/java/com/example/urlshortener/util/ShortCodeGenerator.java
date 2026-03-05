package com.example.urlshortener.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ShortCodeGenerator {

    private static final String BASE62_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int SHORT_CODE_LENGTH = 6;
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateShortCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(BASE62_CHARACTERS.length());
            sb.append(BASE62_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
