package org.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 价格预览响应
 * Price preview response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricePreviewResponse {
    
    /**
     * 产品名称
     * Product name
     */
    private String productName;
    
    /**
     * 原价（美分）
     * Original price in cents
     */
    private Long originalAmount;
    
    /**
     * 小计（美分）= 原价 * 数量
     * Subtotal in cents = original price * quantity
     */
    private Long subtotalAmount;
    
    /**
     * 优惠券代码
     * Coupon code
     */
    private String couponCode;
    
    /**
     * 优惠券名称
     * Coupon name
     */
    private String couponName;
    
    /**
     * 折扣金额（美分）
     * Discount amount in cents
     */
    private Long discountAmount;
    
    /**
     * 最终金额（美分）
     * Final amount in cents
     */
    private Long finalAmount;
    
    /**
     * 货币类型
     * Currency
     */
    private String currency;
    
    /**
     * 数量
     * Quantity
     */
    private Integer quantity;
    
    /**
     * 计费周期
     * Billing interval
     */
    private String billingInterval;
    
    /**
     * 优惠券类型：percent/amount
     * Coupon type: percent/amount
     */
    private String couponType;
    
    /**
     * 优惠券值（百分比或固定金额）
     * Coupon value (percentage or fixed amount)
     */
    private String couponValue;
    
    /**
     * 原价（美元格式化）
     * Original price formatted
     */
    private String originalAmountFormatted;
    
    /**
     * 小计（美元格式化）
     * Subtotal formatted
     */
    private String subtotalAmountFormatted;
    
    /**
     * 折扣金额（美元格式化）
     * Discount amount formatted
     */
    private String discountAmountFormatted;
    
    /**
     * 最终金额（美元格式化）
     * Final amount formatted
     */
    private String finalAmountFormatted;
}
