package org.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeConfig {
    @Value("${stripe.privateKey}")
    public String key;
    
    @Value("${stripe.webhookSecret}")
    public String webhookSecret;
}
