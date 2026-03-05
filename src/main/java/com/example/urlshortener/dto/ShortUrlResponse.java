package com.example.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortUrlResponse {
    private String shortUrl;
}
