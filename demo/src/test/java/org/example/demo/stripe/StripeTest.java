package org.example.demo.stripe;



import org.example.demo.config.StripeConfig;
import org.example.demo.service.StripeProductService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.model.Price;

import javax.annotation.Resource;

/**
 * stripe test
 */
@SpringBootTest
public class StripeTest {
    @Resource
    private StripeConfig stripeConfig;
    @Resource
    private StripeProductService productService;

    /**
     * create StripeProduct
     */
    @Test
    public void createProduct() throws StripeException {
        // 构造测试数据
        // Build test data
        org.example.demo.domain.dto.StripeProduct stripeProduct = new org.example.demo.domain.dto.StripeProduct();
        stripeProduct.setProductId(1L); // 关联的通用产品ID
        stripeProduct.setName("Starter Subscription");
        stripeProduct.setDescription("HK$120/Month subscription");
        stripeProduct.setUnitAmount(12000L); // 价格：120港币（1200分）
        stripeProduct.setCurrency("hkd");
        stripeProduct.setBillingInterval("month"); // 计费周期：月

        // 调用服务创建产品
        // Call service to create product
        org.example.demo.domain.dto.StripeProduct result = productService.createStripeProduct(stripeProduct);

        // 打印结果
        // Print result
        System.out.println("Success! Stripe Product ID: " + result.getStripeProductId());
        System.out.println("Success! Stripe Price ID: " + result.getStripePriceId());
        System.out.println("Success! Database ID: " + result.getId());
    }
}
