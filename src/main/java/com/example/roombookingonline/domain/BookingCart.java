package com.example.roombookingonline.domain;

import com.example.roombookingonline.convertor.DateTimeConvertor;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BookingCart {
    private final Map<Long, List<Integer>> bookingCart = new HashMap<>();
    private LocalDateTime start ;
    private LocalDateTime end ;

    public BookingCart(){
        DateTimeFormatter formatter = DateTimeConvertor.singleFormatter();
        String startD = LocalDateTime.now().plusDays(1).format(formatter);
        String endD = LocalDateTime.now().plusDays(2).format(formatter);
        start = LocalDateTime.parse(startD, formatter);
        end = LocalDateTime.parse(endD, formatter);
    }

    public void addRoom(Long roomTypeId, int adults, int childs) {
        Integer capacity = adults*10 + childs;
        List<Integer> value = new ArrayList<>();
        if(bookingCart.containsKey(roomTypeId)){
            value = bookingCart.get(roomTypeId);
        }
        value.add(capacity);
        bookingCart.put(roomTypeId, value);
    }
    public void updateRoom(Long roomTypeId, int capacityRoom, Integer capacity, int adults, int childs) {
        if(adults+childs > capacityRoom){
            adults = capacityRoom/2;
            childs = capacityRoom - adults;
        }
        Integer capacityNew = adults*10 + childs;
        List<Integer> value = bookingCart.get(roomTypeId);
        value.remove(capacity);
        value.add(capacityNew);
        bookingCart.put(roomTypeId, value);
    }

    public void removeRoom(Long roomTypeId, Integer capacity) {
        List<Integer> value = bookingCart.get(roomTypeId);
        value.remove(capacity);
        if(value.isEmpty()){
            bookingCart.remove(roomTypeId);
        }
    }

    public void emptyCart(){
        bookingCart.clear();
    }

    public Map<Long, List<Integer>> getBookingCart() {
        return bookingCart;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public long getDuration(){
        Duration duration = Duration.between(start, end);
        return duration.toHours();
    }
}
