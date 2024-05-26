package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "room_type_image")
public class RoomTypeImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomTypeEntity roomTypeEntity;
    @Enumerated(EnumType.STRING)
    private photoName name;
    private String description;
    @Column(name = "photo_path")
    private String photoPath;

    public enum photoName{
        front, back, bath, bed
    }
    public List<photoName> getAllPhotoName(){
        List<photoName> statusEnums = Stream.of(photoName.values())
                .collect(Collectors.toList());
        return statusEnums;
    }

    public String getPhoto(){
        if(photoPath == null || photoPath.isEmpty()){
            return null;
        }
//        if (photoPath == null || photos.isBlank()) return "/resources/products-image/default/default-photos.jpg";
        return "/resources/room-type-images/" + name + "/" + roomTypeEntity.getId() + "/" + photoPath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public RoomTypeEntity getRoomTypeEntity() {
        return roomTypeEntity;
    }

    public void setRoomTypeEntity(RoomTypeEntity roomTypeEntity) {
        this.roomTypeEntity = roomTypeEntity;
    }

    public photoName getName() {
        return name;
    }

    public void setName(photoName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
