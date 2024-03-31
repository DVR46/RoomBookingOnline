package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity accountEntity;
    @OneToMany(fetch = FetchType.EAGER)
    private List<RoomEntity> roomEntities;
    @Column(name = "coupon_code")
    private String couponCode;
    private double amount;
    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;
    private LocalDateTime duration;
    private String code;
    @Column(name = "message_services")
    private boolean messageServices;
    @Column(name = "room_service")
    private boolean roomService;
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public AccountEntity getAccountEntity() {
        return accountEntity;
    }

    public void setAccountEntity(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }


    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getDuration() {
        return duration;
    }

    public void setDuration(LocalDateTime duration) {
        this.duration = duration;
    }

    public boolean isMessageServices() {
        return messageServices;
    }

    public void setMessageServices(boolean messageServices) {
        this.messageServices = messageServices;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isRoomService() {
        return roomService;
    }

    public void setRoomService(boolean roomService) {
        this.roomService = roomService;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<RoomEntity> getRoomEntities() {
        return roomEntities;
    }

    public void setRoomEntities(List<RoomEntity> roomEntities) {
        this.roomEntities = roomEntities;
    }
}
