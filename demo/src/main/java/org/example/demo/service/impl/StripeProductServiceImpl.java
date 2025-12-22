package org.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.config.StripeConfig;
import org.example.demo.domain.dto.StripeProduct;
import org.example.demo.domain.enums.BillingInterval;
import org.example.demo.mapper.StripeProductMapper;
import org.example.demo.service.StripeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Stripe产品服务实现类
 * Stripe product service implementation
 */
@Service
public class StripeProductServiceImpl extends ServiceImpl<StripeProductMapper, StripeProduct> implements StripeProductService {

    private static final Logger logger = LogManager.getLogger(StripeProductServiceImpl.class);

    @Autowired
    private StripeConfig stripeConfig;

    @Override
    public List<StripeProduct> listPage() {
        return this.list();
    }

    /**
     * 创建Stripe产品并保存到数据库
     * Create Stripe product and save to database
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StripeProduct createStripeProduct(StripeProduct stripeProduct) throws StripeException {
        logger.info("开始创建Stripe产品: {}", stripeProduct.getName());

        // 设置Stripe API Key
        Stripe.apiKey = stripeConfig.key;

        // 创建Stripe产品
        ProductCreateParams productParams = ProductCreateParams.builder()
                .setName(stripeProduct.getName())
                .setDescription(stripeProduct.getDescription())
                .build();
        Product product = Product.create(productParams);
        logger.info("Stripe产品创建成功, productId: {}", product.getId());

        // Create Stripe price
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setProduct(product.getId())
                .setCurrency(stripeProduct.getCurrency() != null ? stripeProduct.getCurrency() : "hkd")
                .setUnitAmount(stripeProduct.getUnitAmount())
                .setRecurring(
                        PriceCreateParams.Recurring.builder()
                                .setInterval(BillingInterval.toStripeInterval(stripeProduct.getBillingInterval()))
                                .build())
                .build();
        Price price = Price.create(priceParams);
        logger.info("Stripe价格创建成功, priceId: {}", price.getId());

        // 保存到数据库
        stripeProduct.setStripeProductId(product.getId());
        stripeProduct.setStripePriceId(price.getId());
        this.save(stripeProduct);

        logger.info("产品保存到数据库成功, id: {}", stripeProduct.getId());

        return stripeProduct;
    }

}
