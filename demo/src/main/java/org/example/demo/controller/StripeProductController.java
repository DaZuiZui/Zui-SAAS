package org.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stripe.exception.StripeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.common.response.ResponseCode;
import org.example.demo.domain.dto.StripeProduct;
import org.example.demo.service.StripeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Stripe产品控制器
 * Stripe product controller
 */
@RestController
@RequestMapping("/api/stripe/products")
public class StripeProductController {

    private static final Logger logger = LogManager.getLogger(StripeProductController.class);

    @Autowired
    private StripeProductService stripeProductService;

    /**
     * 查询所有Stripe产品
     * Query all Stripe products
     */
    @GetMapping
    public ResponseCode<List<StripeProduct>> list() {
        logger.info("Fetching all stripe products");
        return ResponseCode.buildResponse(stripeProductService.list());
    }

    /**
     * 分页查询Stripe产品
     * Query Stripe products with pagination
     */
    @GetMapping("/page")
    public ResponseCode<Page<StripeProduct>> page(@RequestParam(defaultValue = "1") Integer current,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        logger.info("Fetching stripe products page: {}, size: {}", current, size);
        return ResponseCode.buildResponse(stripeProductService.page(new Page<>(current, size)));
    }

    /**
     * 根据ID查询Stripe产品
     * Query Stripe product by ID
     */
    @GetMapping("/{id}")
    public ResponseCode<StripeProduct> getById(@PathVariable Long id) {
        logger.info("Fetching stripe product by id: {}", id);
        return ResponseCode.buildResponse(stripeProductService.getById(id));
    }

    /**
     * 根据通用产品ID查询Stripe产品
     * Query Stripe products by product ID
     */
    @GetMapping("/product/{productId}")
    public ResponseCode<List<StripeProduct>> getByProductId(@PathVariable Long productId) {
        logger.info("Fetching stripe products by product id: {}", productId);
        QueryWrapper<StripeProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        return ResponseCode.buildResponse(stripeProductService.list(queryWrapper));
    }

    /**
     * 根据Stripe产品ID查询
     * Query by Stripe product ID
     */
    @GetMapping("/stripe/{stripeProductId}")
    public ResponseCode<StripeProduct> getByStripeProductId(@PathVariable String stripeProductId) {
        logger.info("Fetching stripe product by stripe product id: {}", stripeProductId);
        QueryWrapper<StripeProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stripe_product_id", stripeProductId);
        return ResponseCode.buildResponse(stripeProductService.getOne(queryWrapper));
    }

    /**
     * 创建Stripe产品
     * Create Stripe product
     */
    @PostMapping
    public ResponseCode<StripeProduct> save(@RequestBody StripeProduct stripeProduct) throws StripeException {
        logger.info("Creating new stripe product: {}", stripeProduct.getName());

        StripeProduct result = stripeProductService.createStripeProduct(stripeProduct);
        return ResponseCode.buildResponse(result);

    }

    /**
     * 更新Stripe产品
     * Update Stripe product
     */
    @PutMapping
    public ResponseCode<Boolean> update(@RequestBody StripeProduct stripeProduct) {
        logger.info("Updating stripe product: {}", stripeProduct.getId());
        return ResponseCode.buildResponse(stripeProductService.updateById(stripeProduct));
    }

    /**
     * 删除Stripe产品
     * Delete Stripe product
     */
    @DeleteMapping("/{id}")
    public ResponseCode<Boolean> delete(@PathVariable Long id) {
        logger.info("Deleting stripe product: {}", id);
        return ResponseCode.buildResponse(stripeProductService.removeById(id));
    }
}
