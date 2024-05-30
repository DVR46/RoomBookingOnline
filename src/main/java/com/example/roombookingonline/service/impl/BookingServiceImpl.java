package com.example.roombookingonline.service.impl;

import com.example.roombookingonline.entity.*;
import com.example.roombookingonline.repository.CustomerRepository;
import com.example.roombookingonline.repository.OrderRepository;
import com.example.roombookingonline.repository.RoomBookingRepository;
import com.example.roombookingonline.repository.RoomDetailRepository;
import com.example.roombookingonline.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private RoomBookingRepository roomBookingRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoomDetailRepository roomDetailRepository;

    @Override
    public RoomBookingEntity booking(OrderEntity order){
        if(order.getRoomBookingEntity()!=null || !order.isActive()
                || LocalDateTime.now().isAfter(order.getEndDatetime().minusMinutes(30))){
            return null;
        }
        List<CustomerEntity> customerEntities = new ArrayList<>();
        for(int i = 1; i <= (order.getAdults()+ order.getChilds()); i++){
            customerEntities.add(new CustomerEntity());
        }
        RoomBookingEntity roomBookingEntity = new RoomBookingEntity();
        roomBookingEntity.setCustomers(customerEntities);
        return roomBookingEntity;
    }
    @Override
    public RoomBookingEntity bookingSave(RoomBookingEntity roomBookingEntity){
        List<CustomerEntity> customerSave = new ArrayList<>();
        for(CustomerEntity c: roomBookingEntity.getCustomers()){
            if(c.getIdCartNo()==null){
                customerSave.add(c);
                continue;
            }
            CustomerEntity cus = customerRepository.findByIdCartNo(c.getIdCartNo());
            customerSave.add(Objects.requireNonNullElse(cus, c));
        }
        roomBookingEntity.setCustomers(customerRepository.saveAll(customerSave));
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
            r.getRoomDetailEntities().setStatus(RoomDetailEntity.Status.checkIn);
            roomDetailRepository.save(r.getRoomDetailEntities());
        }
    }

    @Override
    public void checkedIn(Long bookingId){
        RoomBookingEntity roomBookingEntity = roomBookingRepository.findById(bookingId).orElseThrow();
        for(RoomOrderEntity r : roomBookingEntity.getOrderEntity().getRoomOrderEntities()){
            r.getRoomDetailEntities().setStatus(RoomDetailEntity.Status.occupied);
            roomDetailRepository.save(r.getRoomDetailEntities());
        }
    }
    @Override
    public RoomBookingEntity checkOut(Long bookingId){
        RoomBookingEntity roomBookingEntity = roomBookingRepository.findById(bookingId).orElseThrow();
        OrderEntity orderEntity = roomBookingEntity.getOrderEntity();
        orderEntity.setActive(false);
        orderRepository.save(orderEntity);
        roomBookingEntity.setCheckOut(LocalDateTime.now());
        List<CustomerEntity> customerSave = new ArrayList<>();
        List<CustomerEntity> customerDelete = new ArrayList<>();
        for(CustomerEntity c : roomBookingEntity.getCustomers()){
            if(c.getName().isEmpty()&&c.getAge()==0){
                customerDelete.add(c);
            }
            else {
                customerSave.add(c);
            }
        }
        if(customerSave.size()!=roomBookingEntity.getCustomers().size()){
            roomBookingEntity.setCustomers(customerSave);
        }
        if(!customerDelete.isEmpty()){
            customerRepository.deleteAll(customerDelete);
        }
        for(RoomOrderEntity r : roomBookingEntity.getOrderEntity().getRoomOrderEntities()){
            r.getRoomDetailEntities().setStatus(RoomDetailEntity.Status.vacant);
            roomDetailRepository.save(r.getRoomDetailEntities());
//            RoomDetailEntity roomDetailEntity = r.getRoomDetailEntities();
//            roomDetailEntity.setStatus(RoomDetailEntity.Status.vacant);
//            roomDetailRepository.save(roomDetailEntity);
        }
        return roomBookingRepository.save(roomBookingEntity);
    }
    @Override
    public RoomBookingEntity findById(Long id){
        return roomBookingRepository.findById(id).orElseThrow();
    }

    @Override
    public List<RoomBookingEntity> getBookingHistory() {
        return roomBookingRepository.findBookingHistory();
    }

    @Override
    public void updateCustomer(RoomBookingEntity roomBooking) {
        RoomBookingEntity roomBookingEntity = roomBookingRepository.findById(roomBooking.getReservationNumber()).orElseThrow();
        List<CustomerEntity> customerEntities = new ArrayList<>();
        for(CustomerEntity c: roomBooking.getCustomers()){
            if(c.getIdCartNo()==null){
                customerEntities.add(c);
                continue;
            }
            CustomerEntity cus = customerRepository.findByIdCartNo(c.getIdCartNo());
            customerEntities.add(Objects.requireNonNullElse(cus, c));
        }
        roomBookingEntity.setCustomers(customerRepository.saveAll(customerEntities));
        roomBookingRepository.save(roomBookingEntity);
    }
}
