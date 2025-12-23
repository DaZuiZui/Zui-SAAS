package org.example.demo.domain.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.demo.domain.entity.BaseEntity;

/**
 * Stripe平台产品表
 * Stripe platform product table
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("stripe_product")
public class StripeProduct extends BaseEntity {

    /**
     * 关联的通用产品ID
     */
    private Long productId;

    /**
     * Stripe平台的产品ID
     */
    private String stripeProductId;

    /**
     * Stripe平台的价格ID
     */
    private String stripePriceId;

    /**
     * 产品名称（Stripe平台）
     */
    private String name;

    /**
     * 产品描述（Stripe平台）
     */
    private String description;

    /**
     * 价格（分）
     */
    private Long unitAmount;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 计费周期：month/year
     */
    private String billingInterval;
}

