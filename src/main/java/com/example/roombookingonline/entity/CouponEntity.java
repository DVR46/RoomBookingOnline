package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "coupon")
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @ManyToOne
    @JoinColumn(name = "coupon_type_id")
    private CouponTypeEntity couponTypeEntity;
    private int quantity;
    private LocalDate startDate;
    private LocalDate endDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CouponTypeEntity getCouponTypeEntity() {
        return couponTypeEntity;
    }

    public void setCouponTypeEntity(CouponTypeEntity couponTypeEntity) {
        this.couponTypeEntity = couponTypeEntity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
