package com.example.roombookingonline.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room_invoice")
public class RoomInvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_name")
    private String status;
    private double amount;
    @OneToOne
    @JoinColumn(name = "reservation_number")
    private RoomBookingEntity roomBookingEntity;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
