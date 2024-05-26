package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.RoomDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface RoomDetailRepository extends JpaRepository<RoomDetailEntity, Long> {
    @Query(value = "select ro.roomDetailEntity.id from RoomOrderEntity ro " +
            "where ro.orderEntity.startDatetime < ?2 and " +
            "ro.orderEntity.endDatetime > ?1 " +
            "and ro.orderEntity.active = true ")
    List<Long> findRoomIsUsed(LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query(value = "from RoomDetailEntity rd " +
            "where rd.id not in ?1 and rd.roomEntity.roomTypeEntity.active = true " +
            "and rd.roomEntity.active = true")
    List<RoomDetailEntity> findRoomIsEmpty(List<Long> ids);

    @Query(value = "select rd.id from RoomDetailEntity rd " +
            "where rd.id not in ?1 and rd.roomEntity.roomTypeEntity.id = ?2 " +
            "and rd.roomEntity.roomTypeEntity.active = true")
    List<Long> findRoomIsEmptyForOrder(List<Long> ids, Long roomTypeId);

    @Query(value = "select ro.roomDetailEntity.id " +
            "from RoomOrderEntity ro " +
            "where ro.roomDetailEntity.id in ?1 and ro.orderEntity.endDatetime < ?2 " +
            "order by timediff(?2, ro.orderEntity.endDatetime) limit 1")
    Long findRoomForOrder(List<Long> ids, LocalDateTime startDatetime);

//    @Query(value = "select ro.roomDetailEntity.id from RoomOrderEntity ro " +
//            "where ro.roomDetailEntity.id in ?1 " +
//            "and (date(ro.orderEntity.startDatetime) and date(ro.orderEntity.endDatetime)) = date(?2)")
//    List<Long> findRoomEmptyDayByIdIn(List<Long> ids, LocalDateTime startDate);


}
