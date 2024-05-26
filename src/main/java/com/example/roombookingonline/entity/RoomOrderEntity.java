package com.example.roombookingonline.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room_order")
public class RoomOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomDetailEntity roomDetailEntity;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;
    private int adults;
    private int childs;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public RoomDetailEntity getRoomDetailEntities() {
        return roomDetailEntity;
    }

    public void setRoomDetailEntities(RoomDetailEntity roomDetailEntityEntity) {
        this.roomDetailEntity = roomDetailEntityEntity;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChilds() {
        return childs;
    }

    public void setChilds(int childs) {
        this.childs = childs;
    }
}
