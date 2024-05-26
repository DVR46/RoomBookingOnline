package com.example.roombookingonline.service.impl;

import com.example.roombookingonline.entity.CouponEntity;
import com.example.roombookingonline.entity.CouponTypeEntity;
import com.example.roombookingonline.repository.CouponRepository;
import com.example.roombookingonline.repository.CouponTypeRepository;
import com.example.roombookingonline.repository.RoomTypeRepository;
import com.example.roombookingonline.service.CouponService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CouponTypeRepository couponTypeRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Override
    public CouponEntity findCouponToUse(String code, Long roomTypeId){
        CouponEntity couponEntity = couponRepository.findByCodeAndRoomType(code, roomTypeId);
        if(couponEntity == null){
            couponEntity = new CouponEntity();
        }
        return couponEntity;
    }

    @Override
    public CouponEntity findByCode(String code){
        return couponRepository.findByCode(code);
    }


    @Override
    public List<CouponTypeEntity> getAllCouponType() {
        return couponTypeRepository.findAll();
    }

    @Override
    public void saveCoupon(CouponEntity couponEntity) {
        couponEntity.setCouponTypeEntity(couponTypeRepository.findById(couponEntity.getCouponTypeEntity().getId()).orElseThrow());
        couponRepository.save(couponEntity);
    }

    @Override
    public void deleteCoupon(Long couponId) {
        couponRepository.deleteById(couponId);
    }

    @Override
    public void saveCouponType(CouponTypeEntity couponTypeEntity) {
        couponTypeEntity.setRoomTypeEntity(roomTypeRepository.findById(couponTypeEntity.getRoomTypeEntity().getId()).orElseThrow());
        couponTypeRepository.save(couponTypeEntity);
    }

    @Override
    @Transactional
    public void deleteCouponType(Long couponTypeId) {
        couponRepository.deleteAll(couponRepository.findByCouponTypeEntity_Id(couponTypeId));
        couponTypeRepository.deleteById(couponTypeId);
    }

    @Override
    public List<CouponEntity> getAllCoupon() {
        return couponRepository.findAll();
    }
}
