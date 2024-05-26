package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.MassageServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MassageServiceRepository extends JpaRepository<MassageServiceEntity, Long> {
    @Query(value = "from MassageServiceEntity m " +
            "where m.orderEntity.id = ?1")
    MassageServiceEntity findByOrderId(Long orderId);
}
