package com.connectmac.dev.repository;

import com.connectmac.dev.model.CustomUrl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UrlShortenerRepositoryTest {

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    @Test
    void shouldGetAllUrls() {
        List<CustomUrl> customUrls = urlShortenerRepository.findAll();

        assertNotNull(customUrls);
    }

}
