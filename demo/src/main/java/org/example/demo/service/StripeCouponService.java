package org.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stripe.exception.StripeException;
import org.example.demo.domain.dto.CreateCouponRequest;
import org.example.demo.domain.entity.StripeCoupon;

import java.util.List;

/**
 * Stripe优惠券服务接口
 * Stripe coupon service interface
 */
public interface StripeCouponService extends IService<StripeCoupon> {
    
    /**
     * 创建Stripe优惠券并保存到数据库
     * Create Stripe coupon and save to database
     * 
     * @param request 创建优惠券请求
     * @return 保存后的优惠券信息
     * @throws StripeException Stripe API异常
     */
    StripeCoupon createStripeCoupon(CreateCouponRequest request) throws StripeException;
    
    /**
     * 根据优惠券ID查询
     * Query by coupon ID
     * 
     * @param stripeCouponId Stripe优惠券ID
     * @return 优惠券信息
     */
    StripeCoupon getByStripeCouponId(String stripeCouponId);
    
    /**
     * 查询所有有效的优惠券
     * List all valid coupons
     * 
     * @return 有效优惠券列表
     */
    List<StripeCoupon> listValidCoupons();
    
    /**
     * 同步Stripe优惠券信息到数据库
     * Sync Stripe coupon to database
     * 
     * @param stripeCouponId Stripe优惠券ID
     * @return 同步后的优惠券信息
     * @throws StripeException Stripe API异常
     */
    StripeCoupon syncCouponFromStripe(String stripeCouponId) throws StripeException;
}
