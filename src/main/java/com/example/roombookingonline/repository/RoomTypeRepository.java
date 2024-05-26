package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity, Long>{
    @Query(value = "from RoomTypeEntity rt " +
            "where rt.active = true group by rt.name order by rt.roomBedType.capacity ")
    List<RoomTypeEntity> findRoomType();

    List<RoomTypeEntity> findByNameAndActiveIsTrue(String name);

    @Query(value = "select distinct rt.name from RoomTypeEntity rt " +
            "where rt.active = true order by rt.roomBedType.capacity ")
    List<String> findName();

    @Query(value = "from RoomTypeEntity rt " +
            "where rt.active = true")
    List<RoomTypeEntity> findRoomTypeActive();
}
