package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(mappedBy = "customers")
    private List<RoomBookingEntity> roomBookingEntities;
    @Column(name = "id_cart_no", unique = true)
    private Long idCartNo;
    private String name;
    private int age;
    private String address;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<RoomBookingEntity> getRoomBookingEntities() {
        return roomBookingEntities;
    }

    public void setRoomBookingEntities(List<RoomBookingEntity> roomBookingEntities) {
        this.roomBookingEntities = roomBookingEntities;
    }

    public Long getIdCartNo() {
        return idCartNo;
    }

    public void setIdCartNo(Long idCartNo) {
        this.idCartNo = idCartNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
