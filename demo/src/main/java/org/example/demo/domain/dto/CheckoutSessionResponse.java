package org.example.demo.domain.dto;

import lombok.Data;

/**
 * 结账会话响应DTO
 * Checkout session response DTO
 */
@Data
public class CheckoutSessionResponse {
    
    /**
     * Session ID
     */
    private String sessionId;
    
    /**
     * 支付页面URL
     * Checkout URL
     */
    private String url;
    
    /**
     * Stripe客户ID
     * Stripe customer ID
     */
    private String customerId;
}
