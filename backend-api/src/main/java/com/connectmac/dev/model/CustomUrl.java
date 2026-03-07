package com.connectmac.dev.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CUSTOM_URLS")
public class CustomUrl {

    @Id
    private String alias;

    private String fullUrl;

    private String shortUrl;
}
