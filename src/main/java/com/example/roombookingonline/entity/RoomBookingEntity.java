package com.example.roombookingonline.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "room_booking")
public class RoomBookingEntity {
    @Id
    @Column(name = "reservation_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationNumber;
    @Column(name = "check_in")
    private LocalDateTime checkIn;
    @Column(name = "check_out")
    private LocalDateTime checkOut;
    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;
    @ManyToMany
    private List<CustomerEntity> customers;

    public Long getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(Long reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public List<CustomerEntity> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerEntity> customers) {
        this.customers = customers;
    }
}
