package org.example.demo.service;

import com.stripe.exception.StripeException;
import org.example.demo.domain.dto.PricePreviewRequest;
import org.example.demo.domain.dto.PricePreviewResponse;

/**
 * 价格预览服务接口
 * Price preview service interface
 */
public interface PricePreviewService {
    
    /**
     * 获取价格预览（使用Stripe精准计算）
     * Get price preview with Stripe accurate calculation
     * 
     * @param request 价格预览请求
     * @return 价格预览响应
     * @throws StripeException Stripe API异常
     */
    PricePreviewResponse getPricePreview(PricePreviewRequest request) throws StripeException;
}
