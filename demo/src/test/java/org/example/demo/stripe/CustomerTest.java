package org.example.demo.stripe;

import com.stripe.exception.StripeException;
import org.example.demo.domain.dto.CheckoutSessionResponse;
import org.example.demo.domain.dto.CreateCheckoutSessionRequest;
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

    /**
     * 测试创建结账会话
     * Test create checkout session
     */
    @Test
    public void testCreateCheckoutSession() throws StripeException {
        // 构造测试数据
        // Build test data
        CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest();
        request.setUserId(1002L); // 使用已存在的用户ID
        request.setPriceId("price_1Sh6fVLmuHVoRgEW1neODFnY"); // 使用已创建的价格ID
        request.setSuccessUrl("https://example.com/success?session_id={CHECKOUT_SESSION_ID}");
        request.setCancelUrl("https://example.com/cancel");
        request.setCouponCode("WELCOME10");

        // 调用服务创建结账会话
        // Call service to create checkout session
        CheckoutSessionResponse response = customerService.createCheckoutSession(request);

        // 打印结果
        // Print result
        System.out.println("Success! Session ID: " + response.getSessionId());
        System.out.println("Success! Checkout URL: " + response.getUrl());
        System.out.println("Success! Customer ID: " + response.getCustomerId());
    }

    /**
     * 测试创建带优惠券的结账会话
     * Test create checkout session with coupon
     */
    @Test
    public void testCreateCheckoutSessionWithCoupon() throws StripeException {
        System.out.println("========== 测试创建带优惠券的结账会话 ==========");

        // 构造测试数据
        CreateCheckoutSessionRequest request = new CreateCheckoutSessionRequest();
        request.setUserId(1001L); // 使用已存在的用户ID
        request.setPriceId("price_1Sh5yHLmuHVoRgEWkwERKweq"); // 使用已创建的价格ID
        request.setCouponCode("TEST20PERCENT"); // 使用优惠券代码
        request.setSuccessUrl("https://example.com/success?session_id={CHECKOUT_SESSION_ID}");
        request.setCancelUrl("https://example.com/cancel");

        // 调用服务创建结账会话
        CheckoutSessionResponse response = customerService.createCheckoutSession(request);

        // 打印结果
        System.out.println("\n========== 结账会话创建成功 ==========");
        System.out.println("Session ID: " + response.getSessionId());
        System.out.println("Checkout URL: " + response.getUrl());
        System.out.println("Customer ID: " + response.getCustomerId());
        System.out.println("优惠券: TEST20PERCENT (20% off)");
        System.out.println("\n提示：访问 Checkout URL 完成支付流程");
        System.out.println("=========================================\n");
    }
}
