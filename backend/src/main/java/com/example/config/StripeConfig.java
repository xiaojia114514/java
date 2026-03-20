package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.publishable-key}")
    private String publishableKey;

    @Value("${stripe.secret-key}")
    private String apiKey;
    // Getter 方法（或直接使用字段）
    public String getSecretKey() {
        return secretKey;
    }

    public String getPublishableKey() {
        return publishableKey;
    }
    public String getapiKey() {
        return apiKey;
    }
}