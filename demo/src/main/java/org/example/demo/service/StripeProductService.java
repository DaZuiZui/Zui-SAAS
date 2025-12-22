package org.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.demo.domain.dto.StripeProduct;

import java.util.List;

/**
 * Stripe产品服务接口
 */
public interface StripeProductService extends IService<StripeProduct> {
    List<StripeProduct> listPage();
}
