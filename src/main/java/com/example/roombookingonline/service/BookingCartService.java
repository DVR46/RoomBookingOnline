package com.example.roombookingonline.service;

import com.example.roombookingonline.entity.CouponEntity;
import com.example.roombookingonline.entity.RoomDetailEntity;
import com.example.roombookingonline.entity.RoomTypeEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BookingCartService {

    void setDatime(LocalDateTime start, LocalDateTime end);

    Map<Long, List<Integer>> getOriginBookingCart();

    Map<RoomTypeEntity, List<Integer>> getBookingCart();

    void addToCart(Long roomTypeId, int adults, int childs);

    void updateRoom(Long roomTypeId, int capacityRoom, Integer capacity, int adults, int childs);

    void removeRoomType(Long roomTypeId, Integer capacity);

    void deleteCart();

    long getDuration();

    LocalDateTime getStart();

    LocalDateTime getEnd();

    int getTotalRoom();

    int getTotalAdults();

    int getTotalChilds();

    double getRoomAmount();

    double getAmountDiscount(CouponEntity couponEntity);

    Map<RoomDetailEntity, Integer> getListRoomForOrder();

    double getRoomServiceAmount();

    double getMassageServiceAmount();
}
