package org.example.demo.controller;

import com.stripe.exception.StripeException;
import org.example.demo.domain.dto.CreateCouponRequest;
import org.example.demo.domain.entity.StripeCoupon;
import org.example.demo.service.StripeCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券控制器
 * Coupon Controller
 */
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private StripeCouponService stripeCouponService;

    /**
     * 创建优惠券（同时保存到Stripe和数据库）
     * Create coupon (save to both Stripe and database)
     *
     * @param request 创建优惠券请求
     * @return 创建的优惠券信息
     */
    @PostMapping
    public ResponseEntity<?> createCoupon(@RequestBody CreateCouponRequest request) {
        try {
            StripeCoupon coupon = stripeCouponService.createStripeCoupon(request);
            return ResponseEntity.ok(coupon);
        } catch (StripeException e) {
            return ResponseEntity.badRequest()
                    .body("Stripe error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid request: " + e.getMessage());
        }
    }

    /**
     * 根据Stripe优惠券ID查询
     * Query by Stripe coupon ID
     *
     * @param stripeCouponId Stripe优惠券ID
     * @return 优惠券信息
     */
    @GetMapping("/{stripeCouponId}")
    public ResponseEntity<?> getCoupon(@PathVariable String stripeCouponId) {

            StripeCoupon coupon = stripeCouponService.getByStripeCouponId(stripeCouponId);
            if (coupon == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(coupon);

    }

    /**
     * 获取所有有效的优惠券
     * Get all valid coupons
     *
     * @return 有效优惠券列表
     */
    @GetMapping("/valid")
    public ResponseEntity<?> getValidCoupons() {

            List<StripeCoupon> coupons = stripeCouponService.listValidCoupons();
            return ResponseEntity.ok(coupons);

    }

    /**
     * 从Stripe同步优惠券信息到数据库
     * Sync coupon from Stripe to database
     *
     * @param stripeCouponId Stripe优惠券ID
     * @return 同步后的优惠券信息
     */
    @PostMapping("/sync/{stripeCouponId}")
    public ResponseEntity<?> syncCoupon(@PathVariable String stripeCouponId) {
        try {
            StripeCoupon coupon = stripeCouponService.syncCouponFromStripe(stripeCouponId);
            return ResponseEntity.ok(coupon);
        } catch (StripeException e) {
            return ResponseEntity.badRequest()
                    .body("Stripe error: " + e.getMessage());
        }
    }
}
