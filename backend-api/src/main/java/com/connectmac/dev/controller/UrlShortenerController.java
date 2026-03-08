package com.connectmac.dev.controller;

import com.connectmac.dev.model.CustomUrl;
import com.connectmac.dev.model.ShortenUrlRequest;
import com.connectmac.dev.model.ShortenUrlResponse;
import com.connectmac.dev.service.UrlShortenerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/url-shortener")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @GetMapping("/urls")
    public List<CustomUrl> getAllUrls() {
        List<CustomUrl> urls = urlShortenerService.getAllCustomUrls();

        return urls;
    }

    @GetMapping("/{alias}")
    public ResponseEntity<HttpStatus> redirectUrlByAlias(@PathVariable("alias") String alias) {
        CustomUrl url = urlShortenerService.getCustomUrlByAlias(alias);

        if (url == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .contentType(MediaType.TEXT_HTML)
                .location(URI.create(url.getFullUrl()))
                .build();
    }

    @DeleteMapping("/{alias}")
    public ResponseEntity<HttpStatus> deleteCustomUrlAlias(@PathVariable String alias) {
        boolean status = urlShortenerService.deleteCustomUrlByAlias(alias);
        if(status)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest shortenUrlRequest) {
        CustomUrl customUrl = urlShortenerService.shortenUrlWithCustomAlias(shortenUrlRequest.fullUrl(),
                shortenUrlRequest.customAlias());

        if (customUrl == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ShortenUrlResponse(customUrl.getShortUrl()));
    }

}
