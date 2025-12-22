package org.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stripe.exception.StripeException;
import org.example.demo.domain.dto.StripeProduct;

import java.util.List;

/**
 * Stripe产品服务接口
 * Stripe product service interface
 */
public interface StripeProductService extends IService<StripeProduct> {
    
    List<StripeProduct> listPage();
    
    /**
     * 创建Stripe产品并保存到数据库
     * Create Stripe product and save to database
     * 
     * @param stripeProduct 产品信息
     * @return 保存后的产品信息
     * @throws StripeException Stripe API异常
     */
    StripeProduct createStripeProduct(StripeProduct stripeProduct) throws StripeException;
}
