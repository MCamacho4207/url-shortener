package com.connectmac.dev.service;

import com.connectmac.dev.model.CustomUrl;
import com.connectmac.dev.repository.UrlShortenerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(UrlShortenerService.class)
public class UrlShortenerServiceTest {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @MockBean
    private UrlShortenerRepository urlShortenerRepository;

    @Test
    void shouldFindAllUrlsSuccess() {
        // given
        List<CustomUrl> customUrls = List.of(new CustomUrl("myAlias", "https://www.google.com", "https://www.mydomain.com/myAlias"));
        given(urlShortenerRepository.findAll()).willReturn(customUrls);

        // when
        List<CustomUrl> results = urlShortenerService.getAllCustomUrls();

        // then
        assertThat(results).hasSize(1);
        assertThat(results).containsExactlyInAnyOrder(customUrls.get(0));
    }

    @Test
    void shouldReturnEmptyListGetAllUrlsFailure() {
        // given
        NullPointerException npe = new NullPointerException("Expected exception message");
        given(urlShortenerRepository.findAll()).willThrow(npe);

        /// when
        List<CustomUrl> results = urlShortenerService.getAllCustomUrls();

        // then
        assertThat(results).hasSize(0);
    }

}
