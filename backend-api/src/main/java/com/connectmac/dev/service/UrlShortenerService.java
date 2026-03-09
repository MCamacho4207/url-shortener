package com.connectmac.dev.service;

import com.connectmac.dev.model.CustomUrl;
import com.connectmac.dev.repository.UrlShortenerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private static final String HOST_BASE_URL = "http://localhost:8080/url-shortener/";
    private static final String RANDOM_ALIAS_AVAILABLE_CHARACTERS = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
    private static final Integer ALIAS_LENGTH = 9;
    private static final SecureRandom RANDOM = new SecureRandom();

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
                .map(customUrl -> {
                    urlShortenerRepository.delete(customUrl);
                    return true;
                })
                .orElse(false);
    }

    public CustomUrl shortenUrlWithCustomAlias(String fullUrl, String customAlias) {
        try {
            if (customAlias == null || StringUtils.isBlank(customAlias)) {
                customAlias = generateCustomAlias();
            }

            if (urlShortenerRepository.findById(customAlias).isPresent()) {
                return null;
            }

            return urlShortenerRepository.save(new CustomUrl(customAlias, fullUrl, HOST_BASE_URL+customAlias));
        } catch (Exception e) {
            log.error("Fatal error occurred while attempting to shorten url: %s with alias: %s".formatted(fullUrl, customAlias));
            return null;
        }
    }

    private String generateCustomAlias() {
        String candidateAlias;

        do {
            candidateAlias = randomAlias();
        } while (urlShortenerRepository.existsById(candidateAlias));

        return candidateAlias;
    }

    private String randomAlias() {
        StringBuilder sb = new StringBuilder(ALIAS_LENGTH);
        for (int i = 0; i < ALIAS_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(RANDOM_ALIAS_AVAILABLE_CHARACTERS.length());
            sb.append(RANDOM_ALIAS_AVAILABLE_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

}
