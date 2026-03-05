package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortUrlRequest;
import com.example.urlshortener.dto.ShortUrlResponse;
import com.example.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody ShortUrlRequest request, HttpServletRequest httpRequest) {
        String originalUrl = request.getUrl();
        if (originalUrl == null || originalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid URL provided"));
        }

        String shortCode = urlService.createShortUrl(originalUrl);

        // Construct the full short URL based on host domain
        String baseUrl = httpRequest.getRequestURL().toString().replace(httpRequest.getRequestURI(), "");
        String shortUrl = baseUrl + "/" + shortCode;

        return ResponseEntity.ok(new ShortUrlResponse(shortUrl));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrl(shortCode);

        if (originalUrl == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Short code not found or invalid"));
        }

        HttpHeaders headers = new HttpHeaders();
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "http://" + originalUrl;
        }
        headers.setLocation(URI.create(originalUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redirect
    }
}
