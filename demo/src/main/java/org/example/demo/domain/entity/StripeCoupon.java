package org.example.demo.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Stripe优惠券实体
 * Stripe coupon entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("stripe_coupon")
public class StripeCoupon extends BaseEntity {
    
    /**
     * Stripe平台的优惠券ID
     * Stripe coupon ID
     */
    @TableField("stripe_coupon_id")
    private String stripeCouponId;
    
    /**
     * 优惠券名称
     * Coupon name
     */
    @TableField("name")
    private String name;
    
    /**
     * 优惠券类型：percent/amount
     * Coupon type: percent/amount
     */
    @TableField("coupon_type")
    private String couponType;
    
    /**
     * 百分比折扣（如20.00表示20%）
     * Percent off
     */
    @TableField("percent_off")
    private BigDecimal percentOff;
    
    /**
     * 固定金额折扣（分）
     * Amount off in cents
     */
    @TableField("amount_off")
    private Long amountOff;
    
    /**
     * 货币类型
     * Currency
     */
    @TableField("currency")
    private String currency;
    
    /**
     * 持续时间类型：forever/once/repeating
     * Duration type
     */
    @TableField("duration")
    private String duration;
    
    /**
     * 持续月数（duration为repeating时使用）
     * Duration in months
     */
    @TableField("duration_in_months")
    private Integer durationInMonths;
    
    /**
     * 最大使用次数
     * Max redemptions
     */
    @TableField("max_redemptions")
    private Integer maxRedemptions;
    
    /**
     * 已使用次数
     * Times redeemed
     */
    @TableField("times_redeemed")
    private Integer timesRedeemed;
    
    /**
     * 过期时间（时间戳）
     * Redeem by timestamp
     */
    @TableField("redeem_by")
    private Long redeemBy;
    
    /**
     * 是否有效：1-有效 0-无效
     * Valid status
     */
    @TableField("valid")
    private Boolean valid;
}
