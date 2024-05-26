package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
//    @Query(value = "select od.roomEntities from OrderEntity od " +
//            "where od.startDatetime >= ?1 or od.endDatetime <= ?2 " +
//            "or (od.startDatetime <= ?1 and od.endDatetime >= ?2)")
//    List<RoomEntity> findRoomByDateFromAndDateTo(LocalDateTime dateFrom, LocalDateTime dateTo);

    OrderEntity findByIdAndAccountEntity_IdAndExistIsTrue(long id, long accountId);

    List<OrderEntity> findAllByExistIsTrueAndAccountEntity_Id(long accountId);

    OrderEntity findByCode(String code);
}
