package com.example.roombookingonline.service;

import com.example.roombookingonline.entity.CustomerEntity;

import java.util.List;

public interface CustomerService {
    List<CustomerEntity> getAllCustomer();

    CustomerEntity findById(Long id);
}
