package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.CouponEntity;
import com.example.roombookingonline.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
    @Query(value = "from CouponEntity c " +
            "where c.code = ?1 and c.couponTypeEntity.roomTypeEntity.id = ?2 " +
            "and (now() between c.startDate and c.endDate)" +
            "and c.quantity > 0")
    CouponEntity findByCodeAndRoomType(String code, Long roomTypeId);

    CouponEntity findByCode(String code);

    List<CouponEntity> findByCouponTypeEntity_Id(Long id);
}
