package org.example.demo.domain.dto;

import lombok.Data;

/**
 * 价格预览请求
 * Price preview request
 */
@Data
public class PricePreviewRequest {
    
    /**
     * Stripe价格ID
     * Stripe price ID
     */
    private String stripePriceId;
    
    /**
     * 优惠券代码（可选）
     * Coupon code (optional)
     */
    private String couponCode;
    
    /**
     * 数量
     * Quantity
     */
    private Integer quantity = 1;
}
