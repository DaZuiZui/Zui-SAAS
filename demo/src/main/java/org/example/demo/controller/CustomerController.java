package org.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stripe.exception.StripeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.common.response.ResponseCode;
import org.example.demo.domain.dto.CheckoutSessionResponse;
import org.example.demo.domain.dto.CreateCheckoutSessionRequest;
import org.example.demo.domain.entity.Customer;
import org.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户控制器
 * Customer controller
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private static final Logger logger = LogManager.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    /**
     * 创建Stripe客户
     * Create Stripe customer
     */
    @PostMapping
    public ResponseCode<Customer> createCustomer(@RequestBody Customer customer) {
        logger.info("Creating stripe customer: userId={}, email={}", customer.getUserId(), customer.getEmail());
        try {
            Customer result = customerService.createStripeCustomer(customer);
            return ResponseCode.buildResponse(result);
        } catch (Exception e) {
            logger.error("Failed to create stripe customer", e);
            return new ResponseCode<>(500, "创建Stripe客户失败: " + e.getMessage());
        }
    }


    /**
     * 创建结账会话
     * Create checkout session
     */
    @PostMapping("/checkout-session")
    public ResponseCode<CheckoutSessionResponse> createCheckoutSession(@RequestBody CreateCheckoutSessionRequest request) throws StripeException {
        logger.info("Creating checkout session: userId={}, priceId={}", request.getUserId(), request.getPriceId());

        CheckoutSessionResponse response = customerService.createCheckoutSession(request);
        return ResponseCode.buildResponse(response);

    }
}
