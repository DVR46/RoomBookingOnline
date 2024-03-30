package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "room_type")
public class RoomTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;
    @OneToMany(mappedBy = "roomTypeEntity", fetch = FetchType.LAZY)
    private List<RoomEntity> roomEntiries;
    @OneToMany(mappedBy = "roomTypeEntity")
    private List<RoomTypeImageEntity> roomTypeImageEntities;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<RoomEntity> getRoomEntiries() {
        return roomEntiries;
    }

    public void setRoomEntiries(List<RoomEntity> roomEntiries) {
        this.roomEntiries = roomEntiries;
    }

    public List<RoomTypeImageEntity> getRoomTypeImageEntities() {
        return roomTypeImageEntities;
    }

    public void setRoomTypeImageEntities(List<RoomTypeImageEntity> roomTypeImageEntities) {
        this.roomTypeImageEntities = roomTypeImageEntities;
    }
}
