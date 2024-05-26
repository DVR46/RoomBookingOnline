package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.RoomsServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomsServiceRepository extends JpaRepository<RoomsServiceEntity, Long> {
    @Query(value = "from RoomsServiceEntity r " +
            "where r.orderEntity.id = ?1")
    RoomsServiceEntity findByOrderId(Long orderId);
}
