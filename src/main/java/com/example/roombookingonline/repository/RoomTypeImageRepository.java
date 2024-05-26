package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.RoomTypeEntity;
import com.example.roombookingonline.entity.RoomTypeImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeImageRepository extends JpaRepository<RoomTypeImageEntity, Long> {
    @Query(value = "from RoomTypeImageEntity ri " +
            "where ri.name = ?1 and ri.roomTypeEntity in ?2")
    List<RoomTypeImageEntity> findByNameAndRoomTypeEntityIn(String name, List<RoomTypeEntity> roomTypeEntity);

    @Query(value = "from RoomTypeImageEntity ri " +
            "where ri.roomTypeEntity.id = ?1")
    List<RoomTypeImageEntity> findByRoomTypeId(Long id);
}
