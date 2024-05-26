package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "room_type")
public class RoomTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "bed_id")
    private RoomBedTypeEntity roomBedType;
    private double pricePerNight;
    @OneToOne(mappedBy = "roomType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_type_detail_id")
    private RoomTypeDetailEntity roomTypeDetail;
    @OneToMany(mappedBy = "roomTypeEntity", fetch = FetchType.LAZY)
    private List<RoomEntity> roomEntiries;
    @OneToMany(mappedBy = "roomTypeEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RoomTypeImageEntity> roomTypeImageEntities;
    private boolean active;

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


    public RoomBedTypeEntity getRoomBedType() {
        return roomBedType;
    }

    public void setRoomBedType(RoomBedTypeEntity roomBedType) {
        this.roomBedType = roomBedType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }


    public RoomTypeDetailEntity getRoomTypeDetail() {
        return roomTypeDetail;
    }

    public void setRoomTypeDetail(RoomTypeDetailEntity roomTypeDetail) {
        this.roomTypeDetail = roomTypeDetail;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getActiveRoom(){
        int activeRoom = 0;
        for (RoomEntity roomEntity : roomEntiries) {
            if (roomEntity.isActive()) {
                activeRoom++;
            }
        }
        return activeRoom;
    }

    public int getVacantRoom(){
        int total = 0;
        for(RoomEntity r : getRoomEntiries()){
            if(r.getRoomDetailEntity().getStatus().toString().equals("vacant")){
                total++;
            }
        }
        return total;
    }

    public int getOccupiedRoom(){
        int total = 0;
        for(RoomEntity r : getRoomEntiries()){
            if(r.getRoomDetailEntity().getStatus().toString().equals("occupied")){
                total++;
            }
        }
        return total;
    }

    public int getCheckInRoom(){
        int total = 0;
        for(RoomEntity r : getRoomEntiries()){
            if(r.getRoomDetailEntity().getStatus().toString().equals("checkIn")){
                total++;
            }
        }
        return total;
    }

    public int getCheckOutRoom(){
        int total = 0;
        for(RoomEntity r : getRoomEntiries()){
            if(r.getRoomDetailEntity().getStatus().toString().equals("checkOut")){
                total++;
            }
        }
        return total;
    }

    public static void main(String[] args) {
//        for (String t: getAllTypeName()){
//            System.out.println(t);
//        }
    }
}
