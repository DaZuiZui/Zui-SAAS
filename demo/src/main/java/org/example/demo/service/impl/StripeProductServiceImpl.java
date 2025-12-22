package org.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.domain.dto.StripeProduct;
import org.example.demo.mapper.StripeProductMapper;
import org.example.demo.service.StripeProductService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Stripe产品服务实现类
 */
@Service
public class StripeProductServiceImpl extends ServiceImpl<StripeProductMapper, StripeProduct> implements StripeProductService {

    private static final Logger logger = LogManager.getLogger(StripeProductServiceImpl.class);

    @Override
    public List<StripeProduct> listPage() {
        return this.list();
    }

}
