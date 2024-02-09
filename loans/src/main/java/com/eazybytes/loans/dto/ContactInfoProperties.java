package com.eazybytes.loans.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "contact-info")
public class ContactInfoProperties {
    private Map<String, String> info;
    private List<String> phoneNumber;
    private String implementedDate;

    // Getters and setters
}
