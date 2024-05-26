package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "room_details")
public class RoomDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_number")
    private RoomEntity roomEntity;
    private String password;
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        vacant, occupied, checkIn, checkOut
    }
    public List<Status> getAllStatus(){
        List<Status> statusEnums = Stream.of(Status.values())
                .collect(Collectors.toList());
        return statusEnums;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
