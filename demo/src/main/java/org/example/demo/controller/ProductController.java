package org.example.demo.controller;

import com.stripe.exception.StripeException;
import org.example.demo.domain.dto.PricePreviewRequest;
import org.example.demo.domain.dto.PricePreviewResponse;
import org.example.demo.service.PricePreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 产品控制器
 * Product Controller
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private PricePreviewService pricePreviewService;

    /**
     * 获取价格预览（支持优惠券）
     * Get price preview with optional coupon
     *
     * @param request 价格预览请求
     * @return 价格预览响应
     */
    @PostMapping("/price-preview")
    public ResponseEntity<?> getPricePreview(@RequestBody PricePreviewRequest request) {
        try {
            // 验证必填参数
            if (request.getStripePriceId() == null || request.getStripePriceId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("stripePriceId is required");
            }

            PricePreviewResponse response = pricePreviewService.getPricePreview(request);
            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            return ResponseEntity.badRequest()
                    .body("Stripe error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid request: " + e.getMessage());
        }
    }
}
