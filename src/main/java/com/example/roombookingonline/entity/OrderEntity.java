package com.example.roombookingonline.entity;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity accountEntity;
    @OneToMany(mappedBy = "orderEntity")
    private List<RoomOrderEntity> roomOrderEntities;
    @ManyToOne
    @JoinColumn(name = "coupon_code")
    private CouponEntity couponEntity;
    private double amount;
    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;
    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;
    private String code;
    @Column(name = "massage_services")
    private boolean massageServices;
    @Column(name = "room_service")
    private boolean roomsService;
    @OneToOne(mappedBy = "orderEntity")
    @JoinColumn(name = "service_invoice_id")
    private ServiceInvoiceEntity serviceInvoiceEntity;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    private boolean exist;
    private boolean active;
    @OneToOne(mappedBy = "orderEntity")
    @JoinColumn(name = "reservation_number")
    private RoomBookingEntity roomBookingEntity;
    @Column(name = "amount_refunds")
    private double amountRefunds;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public AccountEntity getAccountEntity() {
        return accountEntity;
    }

    public void setAccountEntity(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }


    public double getAmountRefunds() {
        return amountRefunds;
    }

    public void setAmountRefunds(double amountRefunds) {
        this.amountRefunds = amountRefunds;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(LocalDateTime endDatetime) {
        this.endDatetime = endDatetime;
    }

    public boolean isMassageServices() {
        return massageServices;
    }

    public void setMassageServices(boolean messageServices) {
        this.massageServices = messageServices;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isRoomsService() {
        return roomsService;
    }

    public void setRoomsService(boolean roomService) {
        this.roomsService = roomService;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<RoomOrderEntity> getRoomOrderEntities() {
        return roomOrderEntities;
    }

    public void setRoomOrderEntities(List<RoomOrderEntity> roomOrderEntities) {
        this.roomOrderEntities = roomOrderEntities;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CouponEntity getCouponEntity() {
        return couponEntity;
    }

    public void setCouponEntity(CouponEntity couponEntity) {
        this.couponEntity = couponEntity;
    }

    public RoomBookingEntity getRoomBookingEntity() {
        return roomBookingEntity;
    }

    public void setRoomBookingEntity(RoomBookingEntity roomBookingEntity) {
        this.roomBookingEntity = roomBookingEntity;
    }

    public ServiceInvoiceEntity getServiceInvoiceEntity() {
        return serviceInvoiceEntity;
    }

    public void setServiceInvoiceEntity(ServiceInvoiceEntity serviceInvoiceEntity) {
        this.serviceInvoiceEntity = serviceInvoiceEntity;
    }

    public long getHoursDuration(){
        Duration duration = Duration.between(startDatetime, endDatetime);
        return duration.toHours();
    }

    public String getQR(){
        return "/resources/orders-code/" + id + ".jpg";
    }

    public String getStatus(){
        if(roomBookingEntity == null){
            if(!active & amountRefunds > 0){
                return "Cancelled";
            }
            if(!active){
                return "Expired";
            }
            else {
                return "Waiting to check in";
            }
        }
        else {
            if (roomBookingEntity.getCheckOut() == null){
//                if(roomBookingEntity.getCheckIn().isBefore(startDatetime)){
//                    if(LocalDateTime.now().isAfter(startDatetime)){
//                        return "Staying";
//                    }
//                    return "Check in";
//                }
//                else {
//                    return "Staying";
//                }
                if(roomOrderEntities.get(0).getRoomDetailEntities().getStatus().toString().equals("vacant")){
                    return "Waiting";
                }
                else if(roomOrderEntities.get(0).getRoomDetailEntities().getStatus().toString().equals("checkIn")){
                    return "Check in";
                }
                else {
                    return "Staying";
                }
            }
            else {
                return "Check out";
            }
        }
    }

    public int getAdults(){
        int adults = 0;
        for(RoomOrderEntity roomOrderEntity : roomOrderEntities){
            adults += roomOrderEntity.getAdults();
        }
        return adults;
    }
    public int getChilds(){
        int childs = 0;
        for(RoomOrderEntity roomOrderEntity : roomOrderEntities){
            childs += roomOrderEntity.getChilds();
        }
        return childs;
    }

    public double getRoomAmount(){
        double roomAmount = 0;
        for(RoomOrderEntity ro : roomOrderEntities){
            roomAmount += ro.getRoomDetailEntities().getRoomEntity().getRoomTypeEntity().getPricePerNight()*(getHoursDuration()/8.0);
        }
        return Math.round(roomAmount);
    }

    public double getAmountDiscount(){
        if(couponEntity == null){
            return 0;
        }
        Duration duration = Duration.between(startDatetime, endDatetime);
        long hours = duration.toHours();
        double amount = 0;
        for(RoomOrderEntity room : roomOrderEntities){
            if(Objects.equals(couponEntity.getCouponTypeEntity().getRoomTypeEntity().getId(), room.getRoomDetailEntities().getRoomEntity().getRoomTypeEntity().getId())){
                amount += (room.getRoomDetailEntities().getRoomEntity().getRoomTypeEntity().getPricePerNight()*(hours/8.0))*(couponEntity.getCouponTypeEntity().getDiscount()/100);
            }
        }
        return Math.round(amount);
    }

    public double getRoomServiceAmount(){
        double adultsAmount = getAdults()*(34.5*((double) getHoursDuration() /8));
        double childsAmount = getChilds()*(15*((double) getHoursDuration() /8));
        return Math.round(adultsAmount+childsAmount);
    }
    public double getMassageServiceAmount(){
        double adultsAmount = getAdults()*(50.5*((double) getHoursDuration() /8));
        double childsAmount = getChilds()*(20*((double) getHoursDuration() /8));
        return Math.round(adultsAmount+childsAmount);
    }

    public boolean checkRoomIsVacant(){
        boolean vacant = false;
        if(LocalDateTime.now().isAfter(startDatetime.minusMinutes(30))){
            vacant = true;
            for(RoomOrderEntity roomOrderEntity : roomOrderEntities){
                if(!roomOrderEntity.getRoomDetailEntities().getStatus().toString().equals("vacant")){
                    vacant = false;
                    break;
                }
            }
        }
        return vacant;
    }

    public boolean checkRoomIsReady(){
        boolean ready = false;
        if ((LocalDateTime.now().isAfter(startDatetime) || LocalDateTime.now().isEqual(startDatetime))
                &&LocalDateTime.now().isBefore(endDatetime)){
            ready = true;
            for(RoomOrderEntity roomOrderEntity : roomOrderEntities){
                if(!roomOrderEntity.getRoomDetailEntities().getStatus().toString().equals("checkIn")){
                    ready = false;
                    break;
                }
            }
        }
        return ready;
    }
}
