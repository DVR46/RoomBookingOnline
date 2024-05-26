package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "room_bed_type")
public class RoomBedTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private int capacity;
    private String description;
    @OneToMany(mappedBy = "roomBedType")
    private List<RoomTypeEntity> roomTypeEntities;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RoomTypeEntity> getRoomTypeEntities() {
        return roomTypeEntities;
    }

    public void setRoomTypeEntities(List<RoomTypeEntity> roomTypeEntities) {
        this.roomTypeEntities = roomTypeEntities;
    }
}
