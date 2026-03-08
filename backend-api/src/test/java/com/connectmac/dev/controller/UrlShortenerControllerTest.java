package com.connectmac.dev.controller;

import com.connectmac.dev.ApplicationTestConfig;
import com.connectmac.dev.model.CustomUrl;
import com.connectmac.dev.model.ShortenUrlRequest;
import com.connectmac.dev.service.UrlShortenerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
public class UrlShortenerControllerTest {

    @Autowired
    private UrlShortenerController urlShortenerController;

    @MockBean
    private UrlShortenerService urlShortenerService;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(urlShortenerController)
                .build();
    }

    @Test
    void willReturn200GetAllUrlsSuccess() throws Exception {
        // given
        List<CustomUrl> urls = List.of(new CustomUrl("myAlias", "https://www.google.com", "https://www.mydomain.com/myAlias"));
        given(urlShortenerService.getAllCustomUrls()).willReturn(urls);

        // when
        ResultActions resultCompletes = mockMvc.perform(get("/url-shortener/urls"));

        // then
        resultCompletes.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].alias").value("myAlias"))
                .andExpect(jsonPath("$[0].fullUrl").value("https://www.google.com"))
                .andExpect(jsonPath("$[0].shortUrl").value("https://www.mydomain.com/myAlias"));
    }

    @Test
    void willReturn302RedirectUrlSuccess() throws Exception {
        // given
        String myAlias = "myAlias";
        CustomUrl customUrl = new CustomUrl(myAlias, "https://www.google.com", "https://www.mydomain.com/myAlias");
        given(urlShortenerService.getCustomUrlByAlias(myAlias)).willReturn(customUrl);

        // when
        ResultActions resultCompletes = mockMvc.perform(get("/url-shortener/myAlias"));

        // then
        resultCompletes.andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(redirectedUrl("https://www.google.com"));
    }

    @Test
    void willReturn404RedirectUrlFailure() throws Exception {
        // given
        String myAlias = "myAlias";
        given(urlShortenerService.getCustomUrlByAlias(myAlias)).willReturn(null);

        // when
        ResultActions resultCompletes = mockMvc.perform(get("/url-shortener/myAlias"));

        // then
        resultCompletes.andExpect(status().isNotFound());
    }

    @Test
    void willReturn204DeleteUrlSuccess() throws Exception {
        // given
        String myAlias = "myAlias";
        given(urlShortenerService.deleteCustomUrlByAlias(myAlias)).willReturn(true);

        // when
        ResultActions resultCompletes = mockMvc.perform(delete("/url-shortener/myAlias"));

        // then
        resultCompletes.andExpect(status().isNoContent());
    }

    @Test
    void willReturn404DeleteUrlFailure() throws Exception {
        // given
        String myAlias = "myAlias";
        given(urlShortenerService.deleteCustomUrlByAlias(myAlias)).willReturn(false);

        // when
        ResultActions resultCompletes = mockMvc.perform(delete("/url-shortener/myAlias"));

        // then
        resultCompletes.andExpect(status().isNotFound());
    }

    @Test
    void willReturn201PostShortenUrlSuccess() throws Exception {
        // given
        String myAlias = "myAlias";
        String fullUrl = "https://www.google.com";
        String shortUrl = "https://www.mydomain.com/myAlias";
        ShortenUrlRequest request = new ShortenUrlRequest(myAlias, fullUrl);
        CustomUrl customUrl = new CustomUrl(myAlias, fullUrl, shortUrl);
        given(urlShortenerService.shortenUrlWithCustomAlias(myAlias, fullUrl)).willReturn(customUrl);

        // when
        ResultActions resultCompletes = mockMvc.perform(post("/url-shortener/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));

        // then
        resultCompletes.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.shortUrl").value("https://www.mydomain.com/myAlias"));
    }

    @Test
    void willReturn400PostShortenUrlFailure() throws Exception {
        // given
        String myAlias = "myAlias";
        String fullUrl = "https://www.google.com";
        ShortenUrlRequest request = new ShortenUrlRequest(myAlias, fullUrl);
        given(urlShortenerService.shortenUrlWithCustomAlias(myAlias, fullUrl)).willReturn(null);

        // when
        ResultActions resultCompletes = mockMvc.perform(post("/url-shortener/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));

        // then
        resultCompletes.andExpect(status().isBadRequest());
    }

}
