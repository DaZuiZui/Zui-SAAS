package org.example.demo.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import org.example.demo.config.StripeConfig;
import org.example.demo.domain.dto.CreateCouponRequest;
import org.example.demo.domain.dto.PricePreviewRequest;
import org.example.demo.domain.dto.PricePreviewResponse;
import org.example.demo.domain.entity.StripeCoupon;
import org.example.demo.service.PricePreviewService;
import org.example.demo.service.StripeCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 价格预览测试类
 * Price Preview Test
 */
@SpringBootTest
public class PricePreviewTest {

    @Resource
    private StripeConfig stripeConfig;

    @Resource
    private PricePreviewService pricePreviewService;

    @Resource
    private StripeCouponService stripeCouponService;

    @BeforeEach
    public void setup() {
        Stripe.apiKey = stripeConfig.key;
    }

    /**
     * 测试价格预览 - 无优惠券
     * Test price preview without coupon
     */
    @Test
    public void testPricePreviewWithoutCoupon() throws StripeException {
        System.out.println("========== 测试价格预览（无优惠券） ==========");

        PricePreviewRequest request = new PricePreviewRequest();
        request.setStripePriceId("price_1Sh6fVLmuHVoRgEW1neODFnY"); // 替换为你的实际Price ID
        request.setQuantity(1);

        PricePreviewResponse response = pricePreviewService.getPricePreview(request);

        printResponse(response);
    }

    /**
     * 测试价格预览 - 使用百分比优惠券
     * Test price preview with percentage coupon
     */
    @Test
    public void testPricePreviewWithPercentCoupon() throws StripeException {
        System.out.println("========== 测试价格预览（百分比优惠券） ==========");

        // 先创建一个测试优惠券（20% off）
        String couponId = "FLASH30";

        PricePreviewRequest request = new PricePreviewRequest();
        request.setStripePriceId("price_1Sh6fVLmuHVoRgEW1neODFnY"); // 替换为你的实际Price ID
        request.setCouponCode(couponId);
        request.setQuantity(1);

        PricePreviewResponse response = pricePreviewService.getPricePreview(request);

        printResponse(response);
    }

    /**
     * 测试价格预览 - 使用固定金额优惠券
     * Test price preview with amount off coupon
     */
//    @Test
//    public void testPricePreviewWithAmountCoupon() throws StripeException {
//        System.out.println("========== 测试价格预览（固定金额优惠券） ==========");
//
//        // 先创建一个测试优惠券（减500分）
//        String couponId = createTestAmountCoupon();
//
//        PricePreviewRequest request = new PricePreviewRequest();
//        request.setStripePriceId("price_1QWlRLKLYtCwgmMIiRF1hfmT"); // 替换为你的实际Price ID
//        request.setCouponCode(couponId);
//        request.setQuantity(1);
//
//        PricePreviewResponse response = pricePreviewService.getPricePreview(request);
//
//        printResponse(response);
//    }

    /**
     * 测试价格预览 - 多个数量
     * Test price preview with multiple quantity
     */
    @Test
    public void testPricePreviewWithMultipleQuantity() throws StripeException {
        System.out.println("========== 测试价格预览（多个数量 + 优惠券） ==========");

        String couponId = "FLASH30";

        PricePreviewRequest request = new PricePreviewRequest();
        request.setStripePriceId("price_1QWlRLKLYtCwgmMIiRF1hfmT"); // 替换为你的实际Price ID
        request.setCouponCode(couponId);
        request.setQuantity(3);

        PricePreviewResponse response = pricePreviewService.getPricePreview(request);

        printResponse(response);
    }

    /**
     * 测试价格预览 - 无效的优惠券
     * Test price preview with invalid coupon
     */
    @Test
    public void testPricePreviewWithInvalidCoupon() throws StripeException {
        System.out.println("========== 测试价格预览（无效优惠券） ==========");

        PricePreviewRequest request = new PricePreviewRequest();
        request.setStripePriceId("price_1QWlRLKLYtCwgmMIiRF1hfmT"); // 替换为你的实际Price ID
        request.setCouponCode("INVALID_COUPON_CODE");
        request.setQuantity(1);

        PricePreviewResponse response = pricePreviewService.getPricePreview(request);

        printResponse(response);
    }

    /**
     * 打印响应结果
     * Print response
     */
    private void printResponse(PricePreviewResponse response) {
        System.out.println("\n========== 价格预览结果 ==========");
        System.out.println("产品名称: " + response.getProductName());
        System.out.println("数量: " + response.getQuantity());
        System.out.println("计费周期: " + response.getBillingInterval());
        System.out.println("货币: " + response.getCurrency());
        System.out.println("----------------------------------------");
        System.out.println("原价(单价): " + response.getOriginalAmountFormatted());
        System.out.println("小计: " + response.getSubtotalAmountFormatted());

        if (response.getCouponCode() != null) {
            System.out.println("----------------------------------------");
            System.out.println("优惠券代码: " + response.getCouponCode());
            System.out.println("优惠券名称: " + response.getCouponName());
            System.out.println("优惠券类型: " + response.getCouponType());
            System.out.println("优惠券值: " + response.getCouponValue());
            System.out.println("折扣金额: " + response.getDiscountAmountFormatted());
        }

        System.out.println("----------------------------------------");
        System.out.println("最终金额: " + response.getFinalAmountFormatted());
        System.out.println("=====================================\n");

        // 验证计算正确性
        long expectedFinal = response.getSubtotalAmount() - response.getDiscountAmount();
        assert response.getFinalAmount().equals(expectedFinal) :
            "计算错误: 最终金额应该等于小计减去折扣";
        System.out.println("✓ 计算验证通过");
    }
}
