package org.example.demo.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.CouponRetrieveParams;
import com.stripe.param.PriceRetrieveParams;
import org.example.demo.config.StripeConfig;
import org.example.demo.domain.dto.PricePreviewRequest;
import org.example.demo.domain.dto.PricePreviewResponse;
import org.example.demo.service.PricePreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 价格预览服务实现
 * Price preview service implementation
 */
@Service
public class PricePreviewServiceImpl implements PricePreviewService {
    
    @Autowired
    private StripeConfig stripeConfig;
    
    @Override
    public PricePreviewResponse getPricePreview(PricePreviewRequest request) throws StripeException {
        // 初始化Stripe
        Stripe.apiKey = stripeConfig.key;
        
        // 获取价格信息
        PriceRetrieveParams priceParams = PriceRetrieveParams.builder()
                .addExpand("product")
                .build();
        Price price = Price.retrieve(request.getStripePriceId(), priceParams, null);
        
        // 获取产品信息
        Product product = (Product) price.getProductObject();
        String productName = product != null ? product.getName() : "Unknown Product";
        
        // 原价和数量
        Long unitAmount = price.getUnitAmount();
        Integer quantity = request.getQuantity() != null ? request.getQuantity() : 1;
        String currency = price.getCurrency();
        String billingInterval = price.getRecurring() != null ? 
                price.getRecurring().getInterval() : "one_time";
        
        // 计算小计
        Long subtotalAmount = unitAmount * quantity;
        
        // 初始化响应对象
        PricePreviewResponse.PricePreviewResponseBuilder responseBuilder = PricePreviewResponse.builder()
                .productName(productName)
                .originalAmount(unitAmount)
                .subtotalAmount(subtotalAmount)
                .currency(currency)
                .quantity(quantity)
                .billingInterval(billingInterval);
        
        // 如果有优惠券，获取优惠券信息并计算折扣
        Long discountAmount = 0L;
        Long finalAmount = subtotalAmount;
        
        if (request.getCouponCode() != null && !request.getCouponCode().trim().isEmpty()) {
            try {
                // 获取优惠券信息
                Coupon coupon = Coupon.retrieve(request.getCouponCode());
                
                if (coupon.getValid()) {
                    responseBuilder.couponCode(coupon.getId())
                            .couponName(coupon.getName());
                    
                    // 根据优惠券类型计算折扣
                    if (coupon.getPercentOff() != null) {
                        // 百分比折扣 - 使用Stripe的精确计算方式
                        // Stripe使用 (amount * percent_off / 100) 并向下取整
                        BigDecimal subtotal = new BigDecimal(subtotalAmount);
                        BigDecimal percentOff = coupon.getPercentOff();
                        
                        // Stripe的百分比折扣计算：向下取整到最接近的整数美分
                        discountAmount = subtotal.multiply(percentOff)
                                .divide(new BigDecimal("100"), 0, RoundingMode.FLOOR)
                                .longValue();
                        
                        responseBuilder.couponType("percent")
                                .couponValue(percentOff.toString() + "%");
                        
                    } else if (coupon.getAmountOff() != null) {
                        // 固定金额折扣
                        // 确保货币匹配
                        if (currency.equalsIgnoreCase(coupon.getCurrency())) {
                            discountAmount = Math.min(coupon.getAmountOff() * quantity, subtotalAmount);
                            responseBuilder.couponType("amount")
                                    .couponValue(formatAmount(coupon.getAmountOff(), currency));
                        } else {
                            throw new IllegalArgumentException(
                                "Coupon currency (" + coupon.getCurrency() + 
                                ") does not match price currency (" + currency + ")");
                        }
                    }
                    
                    // 计算最终金额
                    finalAmount = subtotalAmount - discountAmount;
                    
                    // 确保最终金额不为负数
                    if (finalAmount < 0) {
                        finalAmount = 0L;
                        discountAmount = subtotalAmount;
                    }
                }
            } catch (StripeException e) {
                // 优惠券无效或不存在，忽略优惠券
                responseBuilder.couponCode(request.getCouponCode())
                        .couponName("Invalid or expired coupon");
            }
        }
        
        // 设置折扣和最终金额
        responseBuilder.discountAmount(discountAmount)
                .finalAmount(finalAmount);
        
        // 格式化金额显示
        responseBuilder.originalAmountFormatted(formatAmount(unitAmount, currency))
                .subtotalAmountFormatted(formatAmount(subtotalAmount, currency))
                .discountAmountFormatted(formatAmount(discountAmount, currency))
                .finalAmountFormatted(formatAmount(finalAmount, currency));
        
        return responseBuilder.build();
    }
    
    /**
     * 格式化金额（美分转美元）
     * Format amount (cents to dollars)
     * 
     * @param amountInCents 美分金额
     * @param currency 货币类型
     * @return 格式化后的金额字符串
     */
    private String formatAmount(Long amountInCents, String currency) {
        BigDecimal amount = new BigDecimal(amountInCents)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return currency.toUpperCase() + " " + amount.toString();
    }
}
