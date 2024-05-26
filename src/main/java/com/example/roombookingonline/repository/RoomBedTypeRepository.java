package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.RoomBedTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomBedTypeRepository extends JpaRepository<RoomBedTypeEntity, Long> {
    @Query(value = "select rb.name from RoomBedTypeEntity rb ")
    List<String> getAllTypeBed();

    RoomBedTypeEntity findByName(String name);
}
