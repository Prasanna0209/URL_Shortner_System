package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.util.ShortCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ShortCodeGenerator shortCodeGenerator;

    public String createShortUrl(String originalUrl) {
        String shortCode;
        // Ensure uniqueness
        do {
            shortCode = shortCodeGenerator.generateShortCode();
        } while (urlRepository.findByShortCode(shortCode).isPresent());

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortCode(shortCode);

        urlRepository.save(urlMapping);

        return shortCode;
    }

    public String getOriginalUrl(String shortCode) {
        Optional<UrlMapping> optionalUrlMapping = urlRepository.findByShortCode(shortCode);
        if (optionalUrlMapping.isPresent()) {
            UrlMapping urlMapping = optionalUrlMapping.get();
            incrementClickCount(urlMapping);
            return urlMapping.getOriginalUrl();
        }
        return null; // Handle not found in controller
    }

    private void incrementClickCount(UrlMapping urlMapping) {
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        urlRepository.save(urlMapping);
    }
}
