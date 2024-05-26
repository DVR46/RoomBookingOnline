package com.example.roombookingonline.entity;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "massage_service")
public class MassageServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;
    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;
    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;
    private double amount;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(LocalDateTime endDatetime) {
        this.endDatetime = endDatetime;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
