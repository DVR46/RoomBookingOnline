package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.ServiceInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceInvoiceRepository extends JpaRepository<ServiceInvoiceEntity, Long> {
}
