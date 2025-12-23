package org.example.demo.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建优惠券请求
 * Create coupon request
 */
@Data
public class CreateCouponRequest {

    /**
     * 优惠券ID（可选，不填则自动生成）
     * Coupon ID (optional, auto-generated if not provided)
     */
    private String couponId;

    /**
     * 优惠券名称
     * Coupon name
     */
    private String name;

    /**
     * 优惠券类型：percent/amount
     * Coupon type: percent/amount
     */
    private String couponType;

    /**
     * 百分比折扣（couponType为percent时必填）
     * Percent off (required when couponType is percent)
     */
    private BigDecimal percentOff;

    /**
     * 固定金额折扣，单位：分（couponType为amount时必填）
     * Amount off in cents (required when couponType is amount)
     */
    private Long amountOff;

    /**
     * 货币类型（couponType为amount时必填）
     * Currency (required when couponType is amount)
     */
    private String currency;

    /**
     * 持续时间类型：forever/once/repeating
     * Duration type: forever/once/repeating
     * // 示例1：限时促销
     * duration = "once"              // 仅首次付款享受折扣
     * maxRedemptions = 100          // 限前100个客户使用
     *
     * // 示例2：VIP永久折扣
     * duration = "forever"          // 永久享受折扣
     * maxRedemptions = 50           // 仅限50个VIP客户
     *
     * // 示例3：新用户体验
     * duration = "repeating"        // 前3个月享受折扣
     * durationInMonths = 3
     * maxRedemptions = 200          // 限200个新用户
     */
    private String duration;

    /**
     * 持续月数（duration为repeating时必填）
     * Duration in months (required when duration is repeating)
     */
    private Integer durationInMonths;

    /**
     * 最大使用次数（可选）
     * Max redemptions (optional)
     */
    private Integer maxRedemptions;

    /**
     * 过期时间戳（可选）
     * Redeem by timestamp (optional)
     */
    private Long redeemBy;
}
