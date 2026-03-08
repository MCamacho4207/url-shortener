package com.connectmac.dev.service;

import com.connectmac.dev.model.CustomUrl;
import com.connectmac.dev.repository.UrlShortenerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

        // when
        List<CustomUrl> results = urlShortenerService.getAllCustomUrls();

        // then
        assertThat(results).hasSize(0);
    }

    @Test
    void shouldFindUrlByAliasSuccess() {
        // given
        String myAlias = "myAlias";
        CustomUrl customUrl = new CustomUrl(myAlias, "https://www.google.com", "https://www.mydomain.com/myAlias");
        given(urlShortenerRepository.findById(myAlias)).willReturn(Optional.of(customUrl));

        // when
        CustomUrl result = urlShortenerService.getCustomUrlByAlias(myAlias);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(customUrl);
    }

    @Test
    void shouldReturnNullForFindUrlByAliasFailure() {
        // given
        String myAlias = "myAlias";
        given(urlShortenerRepository.findById(myAlias)).willReturn(Optional.empty());

        // when
        CustomUrl result = urlShortenerService.getCustomUrlByAlias(myAlias);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldDeleteUrlByAliasSuccess() {
        // given
        String myAlias = "myAlias";
        CustomUrl customUrl = new CustomUrl(myAlias, "https://www.google.com", "https://www.mydomain.com/myAlias");
        given(urlShortenerRepository.findById(myAlias)).willReturn(Optional.of(customUrl));
        willDoNothing().given(urlShortenerRepository).delete(customUrl);

        // when
        boolean result = urlShortenerService.deleteCustomUrlByAlias(myAlias);

        // then
        assertThat(result).isTrue();
        verify(urlShortenerRepository, times(1)).delete(customUrl);
    }

    @Test
    void shouldFailToDeleteUrlByAliasFailure() {
        // given
        String myAlias = "myAlias";
        CustomUrl customUrl = new CustomUrl(myAlias, "https://www.google.com", "https://www.mydomain.com/myAlias");
        given(urlShortenerRepository.findById(myAlias)).willReturn(Optional.empty());
        willDoNothing().given(urlShortenerRepository).delete(customUrl);

        // when
        boolean result = urlShortenerService.deleteCustomUrlByAlias(myAlias);

        // then
        assertThat(result).isFalse();
        verify(urlShortenerRepository, times(0)).delete(customUrl);
    }

    @Test
    void shouldShortenUrlSuccess() {
        // given
        String myAlias = "myAlias";
        String fullUrl = "https://www.google.com";
        CustomUrl customUrl = new CustomUrl(myAlias, fullUrl, "http://localhost:8080/url-shortener/myAlias");
        given(urlShortenerRepository.findById(myAlias)).willReturn(Optional.empty());
        given(urlShortenerRepository.save(customUrl)).willReturn(customUrl);

        // when
        CustomUrl result  = urlShortenerService.shortenUrlWithCustomAlias(fullUrl, myAlias);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(customUrl);
    }

    @Test
    void shouldReturnNullIfShorteningExistingUrlFailure() {
        // given
        String myAlias = "myAlias";
        String fullUrl = "https://www.google.com";
        CustomUrl customUrl = new CustomUrl(myAlias, fullUrl, "http://localhost:8080/url-shortener/myAlias");
        given(urlShortenerRepository.findById(myAlias)).willReturn(Optional.of(customUrl));

        // when
        CustomUrl result  = urlShortenerService.shortenUrlWithCustomAlias(fullUrl, myAlias);

        // then
        assertThat(result).isNull();
    }

}
