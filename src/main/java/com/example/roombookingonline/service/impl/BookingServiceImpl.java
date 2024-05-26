package com.example.roombookingonline.service.impl;

import com.example.roombookingonline.entity.*;
import com.example.roombookingonline.repository.CustomerRepository;
import com.example.roombookingonline.repository.OrderRepository;
import com.example.roombookingonline.repository.RoomBookingRepository;
import com.example.roombookingonline.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private RoomBookingRepository roomBookingRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public RoomBookingEntity booking(OrderEntity order){
        if(order.getRoomBookingEntity()!=null || !order.isActive()){
            return null;
        }
        List<CustomerEntity> customerEntities = new ArrayList<>();
        for(int i = 1; i <= (order.getAdults()+ order.getChilds()); i++){
            customerEntities.add(new CustomerEntity());
        }
        for(RoomOrderEntity r : order.getRoomOrderEntities()){
            r.getRoomDetailEntities().setStatus(RoomDetailEntity.Status.checkIn);
        }
        RoomBookingEntity roomBookingEntity = new RoomBookingEntity();
        roomBookingEntity.setCustomers(customerEntities);
        return roomBookingEntity;
    }
    @Override
    public RoomBookingEntity bookingSave(RoomBookingEntity roomBookingEntity){
        for(CustomerEntity c : roomBookingEntity.getCustomers()){
            CustomerEntity cus = customerRepository.findByIdCartNo(c.getIdCartNo());
            if(cus != null){
                roomBookingEntity.getCustomers().remove(c);
                roomBookingEntity.getCustomers().add(cus);
            }
        }
        roomBookingEntity.setCustomers(customerRepository.saveAll(roomBookingEntity.getCustomers()));
        roomBookingEntity.setOrderEntity(orderRepository.findById(roomBookingEntity.getOrderEntity().getId()).orElseThrow());
        roomBookingEntity.setCheckIn(LocalDateTime.now());
        return roomBookingRepository.save(roomBookingEntity);
    }

    @Override
    public List<RoomBookingEntity> getCheckInList(){
        return roomBookingRepository.findOrderCheckIn();
    }

    @Override
    public void checkIn(Long bookingId){
        RoomBookingEntity roomBookingEntity = roomBookingRepository.findById(bookingId).orElseThrow();
        for(RoomOrderEntity r : roomBookingEntity.getOrderEntity().getRoomOrderEntities()){
            r.getRoomDetailEntities().setStatus(RoomDetailEntity.Status.occupied);
        }
    }
    @Override
    public RoomBookingEntity checkOut(Long bookingId){
        RoomBookingEntity roomBookingEntity = roomBookingRepository.findById(bookingId).orElseThrow();
        OrderEntity orderEntity = roomBookingEntity.getOrderEntity();
        orderEntity.setActive(false);
        orderRepository.save(orderEntity);
        roomBookingEntity.setCheckOut(LocalDateTime.now());
        for(RoomOrderEntity r : roomBookingEntity.getOrderEntity().getRoomOrderEntities()){
            r.getRoomDetailEntities().setStatus(RoomDetailEntity.Status.checkOut);
        }
        return roomBookingRepository.save(roomBookingEntity);
    }
    @Override
    public RoomBookingEntity findById(Long id){
        return roomBookingRepository.findById(id).orElseThrow();
    }

    @Override
    public List<RoomBookingEntity> getAll() {
        return roomBookingRepository.findBookingHistory();
    }
}
