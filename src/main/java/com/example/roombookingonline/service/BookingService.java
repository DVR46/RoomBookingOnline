package com.example.roombookingonline.service;

import com.example.roombookingonline.entity.OrderEntity;
import com.example.roombookingonline.entity.RoomBookingEntity;

import java.util.List;

public interface BookingService {
    RoomBookingEntity booking(OrderEntity order);

    RoomBookingEntity bookingSave(RoomBookingEntity roomBookingEntity);

    List<RoomBookingEntity> getCheckInList();

    void checkIn(Long bookingId);

    RoomBookingEntity checkOut(Long bookingId);

    RoomBookingEntity findById(Long id);

    List<RoomBookingEntity> getAll();
}
