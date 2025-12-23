package org.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.param.CouponCreateParams;
import org.example.demo.config.StripeConfig;
import org.example.demo.domain.dto.CreateCouponRequest;
import org.example.demo.domain.entity.StripeCoupon;
import org.example.demo.mapper.StripeCouponMapper;
import org.example.demo.service.StripeCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Stripe优惠券服务实现
 * Stripe coupon service implementation
 */
@Service
public class StripeCouponServiceImpl extends ServiceImpl<StripeCouponMapper, StripeCoupon>
        implements StripeCouponService {

    @Autowired
    private StripeConfig stripeConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StripeCoupon createStripeCoupon(CreateCouponRequest request) throws StripeException {
        // 初始化Stripe
        Stripe.apiKey = stripeConfig.key;

        // 验证请求参数
        validateRequest(request);

        // 构建Stripe优惠券创建参数
        CouponCreateParams.Builder paramsBuilder = CouponCreateParams.builder();

        // 设置优惠券ID（如果提供）
        if (request.getCouponId() != null && !request.getCouponId().trim().isEmpty()) {
            paramsBuilder.setId(request.getCouponId());
        }

        // 设置优惠券名称
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            paramsBuilder.setName(request.getName());
        }

        // 设置折扣类型和值
        if ("percent".equalsIgnoreCase(request.getCouponType())) {
            paramsBuilder.setPercentOff(request.getPercentOff());
        } else if ("amount".equalsIgnoreCase(request.getCouponType())) {
            paramsBuilder.setAmountOff(request.getAmountOff());
            paramsBuilder.setCurrency(request.getCurrency().toLowerCase());
        }

        // 设置持续时间
        CouponCreateParams.Duration duration;
        if ("forever".equalsIgnoreCase(request.getDuration())) {
            duration = CouponCreateParams.Duration.FOREVER;
        } else if ("once".equalsIgnoreCase(request.getDuration())) {
            duration = CouponCreateParams.Duration.ONCE;
        } else {
            duration = CouponCreateParams.Duration.REPEATING;
            paramsBuilder.setDurationInMonths(Long.valueOf(request.getDurationInMonths()));
        }
        paramsBuilder.setDuration(duration);

        // 设置最大使用次数
        if (request.getMaxRedemptions() != null) {
            paramsBuilder.setMaxRedemptions(request.getMaxRedemptions().longValue());
        }

        // 设置过期时间
        if (request.getRedeemBy() != null) {
            paramsBuilder.setRedeemBy(request.getRedeemBy());
        }

        // 在Stripe创建优惠券
        Coupon stripeCoupon = Coupon.create(paramsBuilder.build());

        // 转换为数据库实体并保存
        StripeCoupon dbCoupon = convertToEntity(stripeCoupon);
        this.save(dbCoupon);

        return dbCoupon;
    }

    @Override
    public StripeCoupon getByStripeCouponId(String stripeCouponId) {
        LambdaQueryWrapper<StripeCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StripeCoupon::getStripeCouponId, stripeCouponId);
        return this.getOne(wrapper);
    }

    @Override
    public List<StripeCoupon> listValidCoupons() {
        LambdaQueryWrapper<StripeCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StripeCoupon::getValid, true)
               .orderByDesc(StripeCoupon::getCreateAt);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StripeCoupon syncCouponFromStripe(String stripeCouponId) throws StripeException {
        // 初始化Stripe
        Stripe.apiKey = stripeConfig.key;

        // 从Stripe获取优惠券信息
        Coupon stripeCoupon = Coupon.retrieve(stripeCouponId);

        // 检查数据库是否已存在
        StripeCoupon existingCoupon = getByStripeCouponId(stripeCouponId);

        if (existingCoupon != null) {
            // 更新现有记录
            updateFromStripeCoupon(existingCoupon, stripeCoupon);
            this.updateById(existingCoupon);
            return existingCoupon;
        } else {
            // 创建新记录
            StripeCoupon newCoupon = convertToEntity(stripeCoupon);
            this.save(newCoupon);
            return newCoupon;
        }
    }

    /**
     * 验证请求参数
     * Validate request parameters
     */
    private void validateRequest(CreateCouponRequest request) {
        if (request.getCouponType() == null) {
            throw new IllegalArgumentException("Coupon type is required");
        }

        if ("percent".equalsIgnoreCase(request.getCouponType())) {
            if (request.getPercentOff() == null) {
                throw new IllegalArgumentException("Percent off is required for percent coupon");
            }
            if (request.getPercentOff().compareTo(BigDecimal.ZERO) <= 0
                    || request.getPercentOff().compareTo(new BigDecimal("100")) > 0) {
                throw new IllegalArgumentException("Percent off must be between 0 and 100");
            }
        } else if ("amount".equalsIgnoreCase(request.getCouponType())) {
            if (request.getAmountOff() == null) {
                throw new IllegalArgumentException("Amount off is required for amount coupon");
            }
            if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
                throw new IllegalArgumentException("Currency is required for amount coupon");
            }
            if (request.getAmountOff() <= 0) {
                throw new IllegalArgumentException("Amount off must be greater than 0");
            }
        } else {
            throw new IllegalArgumentException("Invalid coupon type. Must be 'percent' or 'amount'");
        }

        if (request.getDuration() == null) {
            throw new IllegalArgumentException("Duration is required");
        }

        if ("repeating".equalsIgnoreCase(request.getDuration())) {
            if (request.getDurationInMonths() == null || request.getDurationInMonths() <= 0) {
                throw new IllegalArgumentException("Duration in months is required for repeating coupon");
            }
        }
    }

    /**
     * 将Stripe优惠券转换为数据库实体
     * Convert Stripe coupon to database entity
     */
    private StripeCoupon convertToEntity(Coupon stripeCoupon) {
        StripeCoupon entity = new StripeCoupon();
        updateFromStripeCoupon(entity, stripeCoupon);
        return entity;
    }

    /**
     * 从Stripe优惠券更新实体信息
     * Update entity from Stripe coupon
     */
    private void updateFromStripeCoupon(StripeCoupon entity, Coupon stripeCoupon) {
        entity.setStripeCouponId(stripeCoupon.getId());
        entity.setName(stripeCoupon.getName());

        // 设置优惠券类型和值
        if (stripeCoupon.getPercentOff() != null) {
            entity.setCouponType("percent");
            entity.setPercentOff(stripeCoupon.getPercentOff());
            entity.setAmountOff(null);
            entity.setCurrency(null);
        } else if (stripeCoupon.getAmountOff() != null) {
            entity.setCouponType("amount");
            entity.setAmountOff(stripeCoupon.getAmountOff());
            entity.setCurrency(stripeCoupon.getCurrency());
            entity.setPercentOff(null);
        }

        // 设置持续时间
        entity.setDuration(stripeCoupon.getDuration());
        entity.setDurationInMonths(stripeCoupon.getDurationInMonths() != null ?
                stripeCoupon.getDurationInMonths().intValue() : null);

        // 设置使用次数
        entity.setMaxRedemptions(stripeCoupon.getMaxRedemptions() != null ?
                stripeCoupon.getMaxRedemptions().intValue() : null);
        entity.setTimesRedeemed(stripeCoupon.getTimesRedeemed() != null ?
                stripeCoupon.getTimesRedeemed().intValue() : 0);

        // 设置过期时间
        entity.setRedeemBy(stripeCoupon.getRedeemBy());

        // 设置有效状态
        entity.setValid(stripeCoupon.getValid());
    }
}
