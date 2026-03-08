package com.connectmac.dev.repository;

import com.connectmac.dev.model.CustomUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlShortenerRepository extends JpaRepository<CustomUrl, String> {
}
