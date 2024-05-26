package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.RoomBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBookingEntity, Long> {
    @Query(value = "from RoomBookingEntity rb " +
            "where rb.orderEntity.active = true order by rb.orderEntity.startDatetime ")
    List<RoomBookingEntity> findOrderCheckIn();

    @Query(value = "from RoomBookingEntity rb " +
            "where rb.orderEntity.active == false order by rb.checkOut desc ")
    List<RoomBookingEntity> findBookingHistory();
}
