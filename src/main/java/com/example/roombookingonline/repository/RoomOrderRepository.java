package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.RoomDetailEntity;
import com.example.roombookingonline.entity.RoomEntity;
import com.example.roombookingonline.entity.RoomOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomOrderRepository extends JpaRepository<RoomOrderEntity, Long> {



}
