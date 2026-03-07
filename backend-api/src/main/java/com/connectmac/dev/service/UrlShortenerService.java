package com.connectmac.dev.service;

import com.connectmac.dev.model.CustomUrl;
import com.connectmac.dev.repository.UrlShortenerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private static final String HOST_BASE_URL = "https://localhost:8080/url-shortener/";

    private final UrlShortenerRepo urlShortenerRepo;

    public List<CustomUrl> getAllCustomUrls() {
        try {
            return List.of(new CustomUrl("firstUrlalias", "http:www.google.com", "wowweee"));
        } catch (Exception e) {
            log.error("Error occurred while retrieving all URLs");
        }

        return new ArrayList<>();
    }

    public CustomUrl getCustomUrlByAlias(String alias) {
        try {
            return new CustomUrl("my-alias", "", "");
        } catch (Exception e) {
            log.error("Fatal error occurred while attempting to delete custom url with alias: %s".formatted(alias));
        }

        return null;
    }

    public boolean deleteCustomUrlByAlias(String alias) {
        try {
            urlShortenerRepo.deleteById(alias);
        } catch (EmptyResultDataAccessException erdaException) {
            return false;
        } catch (Exception e) {
            log.error("Fatal error occurred while attempting to delete custom url with alias: %s".formatted(alias));
            return false;
        }

        return true;
    }

    public boolean shortenUrlWithCustomAlias(String fullUrl, String customAlias) {
        try {
            if (urlShortenerRepo.findById(customAlias).isPresent()) {
                return false;
            }

            urlShortenerRepo.save(new CustomUrl(customAlias, fullUrl, HOST_BASE_URL+customAlias));
        } catch (Exception e) {
            log.error("Fatal error occurred while attempting to shorten url: %s with alias: %s".formatted(fullUrl, customAlias));
            return false;
        }

        return true;
    }

}
