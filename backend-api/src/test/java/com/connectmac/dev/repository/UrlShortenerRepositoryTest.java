package com.connectmac.dev.repository;

import com.connectmac.dev.model.CustomUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
public class UrlShortenerRepositoryTest {

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    private final String myAlias = "myAlias";
    private final String fullUrl = "https://www.google.com";
    private final String shortUrl = "https://www.mydomain.com/myAlias";
    private CustomUrl customUrl;

    @BeforeEach
    void setUp() {
        customUrl = new CustomUrl(myAlias, fullUrl, shortUrl);
        urlShortenerRepository.save(customUrl);
    }

    @Test
    void shouldGetAllUrls() {
        List<CustomUrl> customUrls = urlShortenerRepository.findAll();

        assertThat(customUrls).hasSize(1);
        assertThat(customUrls).containsExactlyInAnyOrder(customUrl);
    }

    @Test
    void shouldFindCustomUrlForGivenAlias() {
        Optional<CustomUrl> customUrl = urlShortenerRepository.findById(myAlias);

        assertThat(customUrl).isNotEqualTo(Optional.empty());
    }

    @Test
    void shouldDeleteCustomUrlForGivenAlias() {
        assertThat(urlShortenerRepository.existsById(myAlias)).isTrue();

        urlShortenerRepository.delete(customUrl);

        assertThat(urlShortenerRepository.existsById(myAlias)).isFalse();
    }

    @Test
    void shouldAddNewCustomUrlWithNewAlias() {
        String newAlias = "newAlias";
        CustomUrl newCustomUrl = new CustomUrl(newAlias, fullUrl, shortUrl);
        assertThat(urlShortenerRepository.existsById(newAlias)).isFalse();

        urlShortenerRepository.save(newCustomUrl);

        assertThat(urlShortenerRepository.existsById(newAlias)).isTrue();
    }

}
