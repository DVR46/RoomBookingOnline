package com.example.roombookingonline.service.impl;

import com.example.roombookingonline.entity.CustomerEntity;
import com.example.roombookingonline.repository.CustomerRepository;
import com.example.roombookingonline.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<CustomerEntity> getAllCustomer(){
        return customerRepository.findAllByNameIsNotEmpty();
    }

    @Override
    public CustomerEntity findById(Long id){
        return customerRepository.findById(id).orElseThrow();
    }

    @Override
    public void update(CustomerEntity customerEntity) {
        customerRepository.save(customerEntity);
    }
}
