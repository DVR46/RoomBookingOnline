package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "coupon")
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;
    @ManyToOne
    @JoinColumn(name = "coupon_type_id")
    private CouponTypeEntity couponTypeEntity;
    private int quantity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @OneToMany(mappedBy = "couponEntity")
    private List<OrderEntity> orderEntities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

}
