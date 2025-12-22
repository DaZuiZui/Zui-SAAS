package org.example.demo.stripe;



import org.example.demo.config.StripeConfig;
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

    /**
     * create StripeProduct
     */
    @Test
    public void createProduct() throws StripeException {
        Stripe.apiKey =  stripeConfig.key;

        ProductCreateParams productParams =
                ProductCreateParams.builder()
                        .setName("Starter Subscription")
                        .setDescription("$12/Month subscription")
                        .build();
        Product product = Product.create(productParams);
        System.out.println("Success! Here is your starter subscription product id: " + product.getId());

        PriceCreateParams params =
                PriceCreateParams
                        .builder()
                        .setProduct(product.getId())
                        .setCurrency("usd")
                        .setUnitAmount(1200L)
                        .setRecurring(
                                PriceCreateParams.Recurring
                                        .builder()
                                        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                        .build())
                        .build();
        Price price = Price.create(params);
        System.out.println("Success! Here is your starter subscription price id: " + price.getId());
    }
}
