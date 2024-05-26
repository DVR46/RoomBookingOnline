package com.example.roombookingonline.service.impl;

import com.example.roombookingonline.convertor.SortMap;
import com.example.roombookingonline.domain.BookingCart;
import com.example.roombookingonline.entity.CouponEntity;
import com.example.roombookingonline.entity.RoomDetailEntity;
import com.example.roombookingonline.entity.RoomTypeEntity;
import com.example.roombookingonline.repository.RoomDetailRepository;
import com.example.roombookingonline.repository.RoomTypeRepository;
import com.example.roombookingonline.service.BookingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingCartServiceImpl implements BookingCartService {
    @Autowired
    private BookingCart bookingCart;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomDetailRepository roomDetailRepository;

    @Override
    public void setDatime(LocalDateTime start, LocalDateTime end){
        bookingCart.setStart(start);
        bookingCart.setEnd(end);
    }
    @Override
    public Map<Long, List<Integer>> getOriginBookingCart(){
        return bookingCart.getBookingCart();
    }

    @Override
    public Map<RoomTypeEntity, List<Integer>> getBookingCart() {
        Map<Long, List<Integer>> bookingCartMap= bookingCart.getBookingCart();
        Map<RoomTypeEntity, List<Integer>> bookingMap = new HashMap<>();
        for(Map.Entry<Long, List<Integer>> map : bookingCartMap.entrySet()){
            bookingMap.put(roomTypeRepository.findById(map.getKey()).orElseThrow(), map.getValue());
        }
        return SortMap.sortBookingCart(bookingMap);
    }
    @Override
    public void addToCart(Long roomTypeId, int adults, int childs){
        bookingCart.addRoom(roomTypeId, adults, childs);
    }
    @Override
    public void updateRoom(Long roomTypeId, int capacityRoom, Integer capacity, int adults, int childs){
        bookingCart.updateRoom(roomTypeId, capacityRoom, capacity, adults, childs);
    }
    @Override
    public void removeRoomType(Long roomTypeId, Integer capacity){
        bookingCart.removeRoom(roomTypeId, capacity);
    }
    @Override
    public void deleteCart(){
        bookingCart.emptyCart();
    }
    @Override
    public long getDuration(){
        return Math.round(bookingCart.getDuration());
    }
    @Override
    public LocalDateTime getStart(){
        return bookingCart.getStart();
    }
    @Override
    public LocalDateTime getEnd(){
        return bookingCart.getEnd();
    }
    @Override
    public int getTotalRoom(){
        int totalRoom = 0;
        for(Map.Entry<Long, List<Integer>> map : getOriginBookingCart().entrySet()){
            totalRoom += map.getValue().size();
        }
        return totalRoom;
    }
    @Override
    public int getTotalAdults(){
        int adults = 0;
        Map<RoomTypeEntity, List<Integer>> bookingMap = getBookingCart();
        for(Map.Entry<RoomTypeEntity, List<Integer>> map : bookingMap.entrySet()){
            for(Integer i : map.getValue()){
                adults += Integer.parseInt(String.valueOf(i).substring(0,1));
            }
        }
        return adults;
    }
    @Override
    public int getTotalChilds(){
        int childs = 0;
        Map<RoomTypeEntity, List<Integer>> bookingMap = getBookingCart();
        for(Map.Entry<RoomTypeEntity, List<Integer>> map : bookingMap.entrySet()){
            for(Integer i : map.getValue()){
                childs += Integer.parseInt(String.valueOf(i).substring(1));
            }
        }
        return childs;
    }
    @Override
    public double getRoomAmount(){
        Map<RoomTypeEntity, List<Integer>> bookingMap = getBookingCart();
        double roomAmount = 0;
        for (Map.Entry<RoomTypeEntity, List<Integer>> roomMap : bookingMap.entrySet()) {
            roomAmount += (roomMap.getKey().getPricePerNight()*(getDuration()/8.0))*(roomMap.getValue().size());
        }
        return Math.round(roomAmount);
    }
    @Override
    public double getAmountDiscount(CouponEntity couponEntity){
        double amountDiscount = 0;
        Map<RoomTypeEntity, List<Integer>> bookingMap = getBookingCart();
        for (Map.Entry<RoomTypeEntity, List<Integer>> roomMap : bookingMap.entrySet()) {
            if(Objects.equals(roomMap.getKey().getId(), couponEntity.getCouponTypeEntity().getRoomTypeEntity().getId())){
                amountDiscount = (roomMap.getKey().getPricePerNight()*(getDuration()/8.0))*(couponEntity.getCouponTypeEntity().getDiscount()/100);
                amountDiscount *= roomMap.getValue().size();
            }
        }
        return Math.round(amountDiscount);
    }

    @Override
    public Map<RoomDetailEntity, Integer> getListRoomForOrder(){
        Map<RoomDetailEntity, Integer> roomDetailEntities = new HashMap<>();
        List<Long> roomDetailsIdListUsed = roomDetailRepository.findRoomIsUsed(getStart().minusHours(2), getEnd().plusHours(2));
        Map<Long, List<Integer>> bookingMap = getOriginBookingCart();
        for (Map.Entry<Long, List<Integer>> roomMap : bookingMap.entrySet()) {
            for(Integer i : roomMap.getValue()){
                List<Long> roomDetailIdListEmpty = roomDetailRepository.findRoomIsEmptyForOrder(roomDetailsIdListUsed, roomMap.getKey());
                Long roomDetailId = roomDetailRepository.findRoomForOrder(roomDetailIdListEmpty, getStart());
                if(roomDetailId == null){
                    Random random = new Random();
                    roomDetailId = roomDetailIdListEmpty.get(random.nextInt(roomDetailIdListEmpty.size()));
                }
                roomDetailEntities.put(roomDetailRepository.findById(roomDetailId).orElseThrow(), i);
            }
        }
        return roomDetailEntities;
    }

    @Override
    public double getRoomServiceAmount(){
        Duration duration = Duration.between(getStart(), getEnd());
        long hours = duration.toHours();
        double adultsAmount = getTotalAdults()*(34.5*((double) hours /8));
        double childsAmount = getTotalChilds()*(15*((double) hours /8));
        return Math.round(adultsAmount+childsAmount);
    }

    @Override
    public double getMassageServiceAmount(){
        Duration duration = Duration.between(getStart(), getEnd());
        long hours = duration.toHours();
        double adultsAmount = getTotalAdults()*(50.5*((double) hours /8));
        double childsAmount = getTotalChilds()*(20*((double) hours /8));
        return Math.round(adultsAmount+childsAmount);
    }

}
