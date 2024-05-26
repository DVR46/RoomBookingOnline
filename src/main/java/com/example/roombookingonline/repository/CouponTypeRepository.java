package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.CouponTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface CouponTypeRepository extends JpaRepository<CouponTypeEntity, Long> {
}
