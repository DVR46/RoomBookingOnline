package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    @Query(value = "select distinct c.idCartNo from CustomerEntity c")
    List<CustomerEntity> findByIdCartDistinct();

    CustomerEntity findByIdCartNo(long idCartNo);

    @Query(value = "from CustomerEntity c " +
            "where c.name!=''")
    List<CustomerEntity> findAllByNameIsNotEmpty();

}
