package com.connectmac.dev.service;

import com.connectmac.dev.model.CustomUrl;
import com.connectmac.dev.repository.UrlShortenerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private static final String HOST_BASE_URL = "http://localhost:8080/url-shortener/";

    private final UrlShortenerRepository urlShortenerRepository;

    public List<CustomUrl> getAllCustomUrls() {
        try {
            return urlShortenerRepository.findAll();
        } catch (Exception e) {
            log.error("Error occurred while retrieving all URLs");
        }

        return new ArrayList<>();
    }

    public CustomUrl getCustomUrlByAlias(String alias) {
        //TODO: Have smarter logic for "urls" alias validation
        try {
            return urlShortenerRepository.findById(alias).orElse(null);
        } catch (Exception e) {
            log.error("Fatal error occurred while attempting to delete custom url with alias: %s".formatted(alias));
        }

        return null;
    }

    public boolean deleteCustomUrlByAlias(String alias) {
        return urlShortenerRepository.findById(alias)
                .map(url -> {
                    urlShortenerRepository.delete(url);
                    return true;
                })
                .orElse(false);
    }

    public CustomUrl shortenUrlWithCustomAlias(String fullUrl, String customAlias) {
        try {
            if (urlShortenerRepository.findById(customAlias).isPresent()) {
                return null;
            }

            return urlShortenerRepository.save(new CustomUrl(customAlias, fullUrl, HOST_BASE_URL+customAlias));
        } catch (Exception e) {
            log.error("Fatal error occurred while attempting to shorten url: %s with alias: %s".formatted(fullUrl, customAlias));
            return null;
        }
    }

}
