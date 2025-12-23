package org.example.demo.domain.dto;

import lombok.Data;

/**
 * 创建结账会话请求DTO
 * Create checkout session request DTO
 */
@Data
public class CreateCheckoutSessionRequest {
    
    /**
     * 用户ID
     * User ID
     */
    private Long userId;
    
    /**
     * 价格ID
     * Price ID
     */
    private String priceId;
    
    /**
     * 支付成功后跳转URL
     * Success redirect URL
     */
    private String successUrl;
    
    /**
     * 取消支付后跳转URL
     * Cancel redirect URL
     */
    private String cancelUrl;
}
