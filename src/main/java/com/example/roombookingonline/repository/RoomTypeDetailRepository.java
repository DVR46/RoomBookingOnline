package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.RoomTypeDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTypeDetailRepository extends JpaRepository<RoomTypeDetailEntity, Long> {
}
