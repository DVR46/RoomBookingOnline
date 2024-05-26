package com.example.roombookingonline.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "room_type_detail")
public class RoomTypeDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "room_type_id")
    private RoomTypeEntity roomType;
    @Column(length = 1000)
    private String content;
    private double size;
    private boolean tv;
    private boolean hairDryer;
    private boolean freeWifi;
    private boolean minibar;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public boolean isHairDryer() {
        return hairDryer;
    }

    public void setHairDryer(boolean hairDryer) {
        this.hairDryer = hairDryer;
    }

    public boolean isFreeWifi() {
        return freeWifi;
    }

    public void setFreeWifi(boolean freeWifi) {
        this.freeWifi = freeWifi;
    }

    public boolean isMinibar() {
        return minibar;
    }

    public void setMinibar(boolean minibar) {
        this.minibar = minibar;
    }
}
