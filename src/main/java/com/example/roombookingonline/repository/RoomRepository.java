package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.RoomEntity;
import com.example.roombookingonline.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query(value = "select r.roomTypeEntity.id from RoomEntity r " +
            "where r.roomDetailEntity.id not in ?1 ")
    List<Long> findRoomTypeNotInRoomDetailId(List<Long> ids);

}
