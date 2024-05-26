package com.example.roombookingonline.entity;

import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Table(name = "rooms")
public class RoomEntity {
    @Id
    @Column(name = "room_number")
    private Long roomNumber;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_type_id")
    private RoomTypeEntity roomTypeEntity;
    @OneToOne(mappedBy = "roomEntity")
    @JoinColumn(name = "room_detail_id")
    private RoomDetailEntity roomDetailEntity;
    private boolean active;

    public Long getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomTypeEntity getRoomTypeEntity() {
        return roomTypeEntity;
    }

    public void setRoomTypeEntity(RoomTypeEntity roomTypeEntity) {
        this.roomTypeEntity = roomTypeEntity;
    }

    public boolean isActive() {
        return active;
    }

    public RoomDetailEntity getRoomDetailEntity() {
        return roomDetailEntity;
    }

    public void setRoomDetailEntity(RoomDetailEntity roomDetailEntity) {
        this.roomDetailEntity = roomDetailEntity;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
