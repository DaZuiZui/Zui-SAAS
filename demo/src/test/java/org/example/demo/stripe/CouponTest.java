package org.example.demo.stripe;

import com.stripe.exception.StripeException;
import org.example.demo.domain.dto.CreateCouponRequest;
import org.example.demo.domain.entity.StripeCoupon;
import org.example.demo.service.StripeCouponService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 优惠券测试类
 * Coupon Test
 */
@SpringBootTest
public class CouponTest {

    @Resource
    private StripeCouponService stripeCouponService;

    /**
     * 测试创建百分比优惠券
     * Test create percent coupon
     */
    @Test
    public void testCreatePercentCoupon() throws StripeException {
        System.out.println("========== 测试创建百分比优惠券 ==========");

        CreateCouponRequest request = new CreateCouponRequest();
        request.setCouponId("SUMMER2025"); // 可选，不填则自动生成
        request.setName("Summer Sale 2025");
        request.setCouponType("percent");
        request.setPercentOff(new BigDecimal("25.00")); // 25% off
        request.setDuration("forever");
        request.setMaxRedemptions(100); // 最多使用100次

        StripeCoupon coupon = stripeCouponService.createStripeCoupon(request);

        System.out.println("优惠券创建成功！");
        printCoupon(coupon);
    }

    /**
     * 测试创建固定金额优惠券
     * Test create amount off coupon
     */
    @Test
    public void testCreateAmountCoupon() throws StripeException {
        System.out.println("========== 测试创建固定金额优惠券 ==========");

        CreateCouponRequest request = new CreateCouponRequest();
        request.setCouponId("WELCOME10"); // 可选
        request.setName("Welcome Bonus");
        request.setCouponType("amount");
        request.setAmountOff(1000L); // 减10港币（1000分）
        request.setCurrency("hkd");
        request.setDuration("once"); // 仅使用一次
        request.setMaxRedemptions(2);

        StripeCoupon coupon = stripeCouponService.createStripeCoupon(request);

        System.out.println("优惠券创建成功！");
        printCoupon(coupon);
    }

    /**
     * 测试创建重复使用的优惠券
     * Test create repeating coupon
     */
    @Test
    public void testCreateRepeatingCoupon() throws StripeException {
        System.out.println("========== 测试创建重复使用的优惠券 ==========");

        CreateCouponRequest request = new CreateCouponRequest();
        request.setName("3 Months Discount");
        request.setCouponType("percent");
        request.setPercentOff(new BigDecimal("15.00")); // 15% off
        request.setDuration("repeating");
        request.setDurationInMonths(3); // 持续3个月

        StripeCoupon coupon = stripeCouponService.createStripeCoupon(request);

        System.out.println("优惠券创建成功！");
        printCoupon(coupon);
    }

    /**
     * 测试创建带过期时间的优惠券
     * Test create coupon with expiration
     */
    @Test
    public void testCreateCouponWithExpiration() throws StripeException {
        System.out.println("========== 测试创建带过期时间的优惠券 ==========");

        // 设置过期时间为30天后
        long expirationTime = System.currentTimeMillis() / 1000 + (30 * 24 * 60 * 60);

        CreateCouponRequest request = new CreateCouponRequest();
        request.setCouponId("FLASH30");
        request.setName("Flash Sale - 30 Days");
        request.setCouponType("percent");
        request.setPercentOff(new BigDecimal("30.00")); // 30% off
        request.setDuration("forever");
        request.setRedeemBy(expirationTime);
        request.setMaxRedemptions(50);

        StripeCoupon coupon = stripeCouponService.createStripeCoupon(request);

        System.out.println("优惠券创建成功！");
        printCoupon(coupon);
    }

    /**
     * 测试查询优惠券
     * Test query coupon
     */
    @Test
    public void testGetCoupon() {
        System.out.println("========== 测试查询优惠券 ==========");

        String couponId = "FLASH30"; // 使用之前创建的优惠券ID
        StripeCoupon coupon = stripeCouponService.getByStripeCouponId(couponId);

        if (coupon != null) {
            System.out.println("找到优惠券：");
            printCoupon(coupon);
        } else {
            System.out.println("未找到优惠券：" + couponId);
        }
    }

    /**
     * 测试查询所有有效优惠券
     * Test list all valid coupons
     */
    @Test
    public void testListValidCoupons() {
        System.out.println("========== 测试查询所有有效优惠券 ==========");

        List<StripeCoupon> coupons = stripeCouponService.listValidCoupons();

        System.out.println("共找到 " + coupons.size() + " 个有效优惠券：");
        for (StripeCoupon coupon : coupons) {
            printCoupon(coupon);
            System.out.println("----------------------------------------");
        }
    }

    /**
     * 测试从Stripe同步优惠券
     * Test sync coupon from Stripe
     */
    @Test
    public void testSyncCouponFromStripe() throws StripeException {
        System.out.println("========== 测试从Stripe同步优惠券 ==========");

        String stripeCouponId = "SUMMER2025"; // Stripe上已存在的优惠券ID
        StripeCoupon coupon = stripeCouponService.syncCouponFromStripe(stripeCouponId);

        System.out.println("同步成功！");
        printCoupon(coupon);
    }

    /**
     * 打印优惠券信息
     * Print coupon information
     */
    private void printCoupon(StripeCoupon coupon) {
        System.out.println("\n========== 优惠券信息 ==========");
        System.out.println("数据库ID: " + coupon.getId());
        System.out.println("Stripe优惠券ID: " + coupon.getStripeCouponId());
        System.out.println("名称: " + coupon.getName());
        System.out.println("类型: " + coupon.getCouponType());

        if ("percent".equals(coupon.getCouponType())) {
            System.out.println("折扣: " + coupon.getPercentOff() + "%");
        } else {
            System.out.println("折扣: " + coupon.getAmountOff() + " " + coupon.getCurrency().toUpperCase());
        }

        System.out.println("持续时间: " + coupon.getDuration());
        if (coupon.getDurationInMonths() != null) {
            System.out.println("持续月数: " + coupon.getDurationInMonths());
        }

        if (coupon.getMaxRedemptions() != null) {
            System.out.println("最大使用次数: " + coupon.getMaxRedemptions());
        }
        System.out.println("已使用次数: " + coupon.getTimesRedeemed());

        if (coupon.getRedeemBy() != null) {
            System.out.println("过期时间: " + new java.util.Date(coupon.getRedeemBy() * 1000));
        }

        System.out.println("是否有效: " + (coupon.getValid() ? "是" : "否"));
        System.out.println("创建时间: " + new java.util.Date(coupon.getCreateAt()));
        System.out.println("=====================================\n");
    }
}
