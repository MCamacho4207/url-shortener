package com.connectmac.dev.controller;

import com.connectmac.dev.ApplicationTestConfig;
import com.connectmac.dev.model.CustomUrl;
import com.connectmac.dev.service.UrlShortenerService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
public class UrlShortenerControllerTest {

    @Autowired
    private UrlShortenerController urlShortenerController;

    @MockBean
    private UrlShortenerService urlShortenerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(urlShortenerController)
                .build();
    }

    @Test
    void willGetAllUrlsSuccess() throws Exception {
        // given
        List<CustomUrl> urls = List.of(new CustomUrl("myAlias", "https://www.google.com", "https://www.mydomain.com/myAlias"));
        given(urlShortenerService.getAllCustomUrls()).willReturn(urls);

        // when then
        ResultActions resultCompletes = mockMvc.perform(get("/url-shortener/urls"));

        // then
        resultCompletes
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].alias").value("myAlias"))
                .andExpect(jsonPath("$[0].fullUrl").value("https://www.google.com"))
                .andExpect(jsonPath("$[0].shortUrl").value("https://www.mydomain.com/myAlias"));
    }

}
