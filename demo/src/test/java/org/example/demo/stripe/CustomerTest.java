package org.example.demo.stripe;

import com.stripe.exception.StripeException;
import org.example.demo.domain.entity.Customer;
import org.example.demo.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Customer测试类
 * Customer test class
 */
@SpringBootTest
public class CustomerTest {
    
    @Resource
    private CustomerService customerService;
    
    /**
     * 测试创建Stripe客户
     * Test create Stripe customer
     */
    @Test
    public void testCreateCustomer() throws StripeException {
        // 构造测试数据
        // Build test data
        Customer customer = new Customer();
        customer.setUserId(1001L);
        customer.setEmail("testuser@example.com");
        customer.setName("Test User");
        customer.setPhone("+85298765432");
        customer.setAddress("Hong Kong");
        
        // 调用服务创建客户
        // Call service to create customer
        Customer result = customerService.createStripeCustomer(customer);
        
        // 打印结果
        // Print result
        System.out.println("Success! Stripe Customer ID: " + result.getStripeCustomerId());
        System.out.println("Success! Database ID: " + result.getId());
        System.out.println("Success! User ID: " + result.getUserId());
    }
}
