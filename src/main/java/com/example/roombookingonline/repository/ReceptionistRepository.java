package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.ReceptionistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistRepository extends JpaRepository<ReceptionistEntity, Long> {
}
