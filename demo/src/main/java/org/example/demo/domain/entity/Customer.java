package org.example.demo.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户表
 * Customer table
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer")
public class Customer extends BaseEntity {

    /**
     * 用户ID
     * User ID
     */
    private Long userId;

    /**
     * Stripe客户ID
     * Stripe customer ID
     */
    private String stripeCustomerId;

    /**
     * 客户邮箱
     * Customer email
     */
    private String email;

    /**
     * 客户名称
     * Customer name
     */
    private String name;

    /**
     * 电话号码
     * Phone number
     */
    private String phone;

    /**
     * 地址
     * Address
     */
    private String address;

    /**
     * 默认支付方式ID
     * Default payment method ID
     */
    private String defaultPaymentMethod;
}
