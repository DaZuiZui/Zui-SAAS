package org.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.common.response.ResponseCode;
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
     * 根据用户ID查询客户
     * Query customer by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseCode<Customer> getByUserId(@PathVariable Long userId) {
        logger.info("Fetching customer by user id: {}", userId);
        Customer customer = customerService.getByUserId(userId);
        return ResponseCode.buildResponse(customer);
    }
    
    /**
     * 查询所有客户
     * Query all customers
     */
    @GetMapping
    public ResponseCode<List<Customer>> list() {
        logger.info("Fetching all customers");
        return ResponseCode.buildResponse(customerService.list());
    }
    
    /**
     * 分页查询客户
     * Query customers with pagination
     */
    @GetMapping("/page")
    public ResponseCode<Page<Customer>> page(@RequestParam(defaultValue = "1") Integer current,
                                              @RequestParam(defaultValue = "10") Integer size) {
        logger.info("Fetching customers page: {}, size: {}", current, size);
        return ResponseCode.buildResponse(customerService.page(new Page<>(current, size)));
    }
    
    /**
     * 根据ID查询客户
     * Query customer by ID
     */
    @GetMapping("/{id}")
    public ResponseCode<Customer> getById(@PathVariable Long id) {
        logger.info("Fetching customer by id: {}", id);
        return ResponseCode.buildResponse(customerService.getById(id));
    }
    
    /**
     * 更新客户
     * Update customer
     */
    @PutMapping
    public ResponseCode<Boolean> update(@RequestBody Customer customer) {
        logger.info("Updating customer: {}", customer.getId());
        return ResponseCode.buildResponse(customerService.updateById(customer));
    }
    
    /**
     * 删除客户
     * Delete customer
     */
    @DeleteMapping("/{id}")
    public ResponseCode<Boolean> delete(@PathVariable Long id) {
        logger.info("Deleting customer: {}", id);
        return ResponseCode.buildResponse(customerService.removeById(id));
    }
}
