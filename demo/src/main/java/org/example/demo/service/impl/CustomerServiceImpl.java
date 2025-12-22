package org.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.CustomerCreateParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.config.StripeConfig;
import org.example.demo.domain.entity.Customer;
import org.example.demo.mapper.CustomerMapper;
import org.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户服务实现类
 * Customer service implementation
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);

    @Autowired
    private StripeConfig stripeConfig;

    /**
     * 创建Stripe客户并保存到数据库
     * Create Stripe customer and save to database
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Customer createStripeCustomer(Customer customer) throws StripeException {
        logger.info("开始创建Stripe客户: userId={}, email={}", customer.getUserId(), customer.getEmail());
        
        // 检查该用户是否已经创建过Customer
        // Check if customer already exists for this user
        Customer existingCustomer = getByUserId(customer.getUserId());
        if (existingCustomer != null) {
            logger.warn("用户已存在Stripe客户记录: userId={}, customerId={}", 
                    customer.getUserId(), existingCustomer.getStripeCustomerId());
            return existingCustomer;
        }
        
        // 设置Stripe API Key
        Stripe.apiKey = stripeConfig.key;
        
        // 构建Stripe Customer创建参数
        // Build Stripe customer create params
        CustomerCreateParams.Builder paramsBuilder = CustomerCreateParams.builder()
                .setEmail(customer.getEmail())
                .putMetadata("userId", customer.getUserId().toString());
        
        if (customer.getName() != null) {
            paramsBuilder.setName(customer.getName());
        }
        if (customer.getPhone() != null) {
            paramsBuilder.setPhone(customer.getPhone());
        }
        if (customer.getAddress() != null) {
            paramsBuilder.setAddress(
                    CustomerCreateParams.Address.builder()
                            .setLine1(customer.getAddress())
                            .build());
        }
        
        // 调用Stripe API创建客户
        // Call Stripe API to create customer
        com.stripe.model.Customer stripeCustomer = com.stripe.model.Customer.create(paramsBuilder.build());
        logger.info("Stripe客户创建成功: customerId={}", stripeCustomer.getId());
        
        // 保存到数据库
        // Save to database
        customer.setStripeCustomerId(stripeCustomer.getId());
        this.save(customer);
        
        logger.info("客户保存到数据库成功: id={}, stripeCustomerId={}", 
                customer.getId(), customer.getStripeCustomerId());
        
        return customer;
    }

    /**
     * 根据用户ID查询客户
     * Query customer by user ID
     */
    @Override
    public Customer getByUserId(Long userId) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.getOne(queryWrapper);
    }
}
