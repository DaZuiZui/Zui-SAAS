package org.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stripe.exception.StripeException;
import org.example.demo.domain.entity.Customer;

/**
 * 客户服务接口
 * Customer service interface
 */
public interface CustomerService extends IService<Customer> {
    
    /**
     * 创建Stripe客户并保存到数据库
     * Create Stripe customer and save to database
     * 
     * @param customer 客户信息
     * @return 保存后的客户信息
     * @throws StripeException Stripe API异常
     */
    Customer createStripeCustomer(Customer customer) throws StripeException;
    
    /**
     * 根据用户ID查询客户
     * Query customer by user ID
     * 
     * @param userId 用户ID
     * @return 客户信息
     */
    Customer getByUserId(Long userId);
}
