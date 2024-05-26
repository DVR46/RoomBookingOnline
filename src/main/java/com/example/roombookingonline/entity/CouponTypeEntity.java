package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "coupon_type")
public class CouponTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomTypeEntity roomTypeEntity;
    private double discount;
    @OneToMany(mappedBy = "couponTypeEntity")
    private List<CouponEntity> couponEntities;
    private String description;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomTypeEntity getRoomTypeEntity() {
        return roomTypeEntity;
    }

    public void setRoomTypeEntity(RoomTypeEntity roomTypeEntity) {
        this.roomTypeEntity = roomTypeEntity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public List<CouponEntity> getCouponEntities() {
        return couponEntities;
    }

    public void setCouponEntities(List<CouponEntity> couponEntities) {
        this.couponEntities = couponEntities;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
