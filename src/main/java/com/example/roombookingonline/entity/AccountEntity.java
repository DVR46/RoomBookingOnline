package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    private String phone;
    private String email;
    @OneToMany(mappedBy = "accountEntity")
    private List<OrderEntity> orderEntities;
    private boolean active;
    private boolean baned;
    private LocalDateTime banedTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isBaned() {
        return baned;
    }

    public void setBaned(boolean baned) {
        this.baned = baned;
    }

    public LocalDateTime getBanedTime() {
        return banedTime;
    }

    public void setBanedTime(LocalDateTime banedTime) {
        this.banedTime = banedTime;
    }

    public String getStatus(){
        if(baned){
            return "Baned";
        }
        else if(active){
            return "Active";
        }
        else {
            return "Inactive";
        }
    }

    public double getTotalOrderAmount(){
        double total = 0;
        for(OrderEntity orderEntity : orderEntities){
            total += orderEntity.getAmount();
        }
        return total;
    }
}
