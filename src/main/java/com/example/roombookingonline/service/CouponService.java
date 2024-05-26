package com.example.roombookingonline.service;

import com.example.roombookingonline.entity.CouponEntity;
import com.example.roombookingonline.entity.CouponTypeEntity;

import java.util.List;

public interface CouponService {

    CouponEntity findCouponToUse(String code, Long roomTypeId);

    CouponEntity findByCode(String id);

    List<CouponEntity> getAllCoupon();

    List<CouponTypeEntity> getAllCouponType();

    void saveCoupon(CouponEntity couponEntity);

    void deleteCoupon(Long couponCode);

    void saveCouponType(CouponTypeEntity couponTypeEntity);

    void deleteCouponType(Long couponTypeId);
}
