package org.example.demo.domain.enums;

import com.stripe.param.PriceCreateParams;

/**
 * 计费周期枚举
 * Billing interval enum
 */
public enum BillingInterval {
    
    /**
     * 月付
     * Monthly billing
     */
    MONTH("month", "月付", PriceCreateParams.Recurring.Interval.MONTH),
    
    /**
     * 年付
     * Yearly billing
     */
    YEAR("year", "年付", PriceCreateParams.Recurring.Interval.YEAR);
    
    /**
     * 代码值
     * Code value
     */
    private final String code;
    
    /**
     * 描述
     * Description
     */
    private final String description;
    
    /**
     * Stripe计费周期
     * Stripe interval
     */
    private final PriceCreateParams.Recurring.Interval stripeInterval;
    
    BillingInterval(String code, String description, PriceCreateParams.Recurring.Interval stripeInterval) {
        this.code = code;
        this.description = description;
        this.stripeInterval = stripeInterval;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public PriceCreateParams.Recurring.Interval getStripeInterval() {
        return stripeInterval;
    }
    
    /**
     * 根据代码获取枚举
     * Get enum by code
     * 
     * @param code 代码值
     * @return 计费周期枚举
     */
    public static BillingInterval fromCode(String code) {
        for (BillingInterval interval : values()) {
            if (interval.code.equalsIgnoreCase(code)) {
                return interval;
            }
        }
        return MONTH; // 默认返回月付
    }
    
    /**
     * 转换为Stripe计费周期
     * Convert to Stripe interval
     * 
     * @param code 代码值
     * @return Stripe计费周期
     */
    public static PriceCreateParams.Recurring.Interval toStripeInterval(String code) {
        return fromCode(code).getStripeInterval();
    }
}
